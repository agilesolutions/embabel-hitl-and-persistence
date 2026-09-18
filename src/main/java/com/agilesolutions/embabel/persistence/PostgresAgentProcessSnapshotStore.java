package com.agilesolutions.embabel.persistence;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PostgreSQL-backed implementation of {@link AgentProcessSnapshotStore}.
 * Uses a simple optimistic locking approach: callers must provide the expected
 * version in the snapshot. Updates increment the stored version by 1.
 */
@Repository
public class PostgresAgentProcessSnapshotStore implements AgentProcessSnapshotStore {

    private static final String TABLE = "agent_process_snapshot";

    private final NamedParameterJdbcTemplate jdbc;

    public PostgresAgentProcessSnapshotStore(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void save(AgentProcessSnapshot snapshot) {
        UUID id = snapshot.getProcessId();
        Optional<AgentProcessSnapshot> existing = load(id);
        Instant now = Instant.now();

        if (existing.isEmpty()) {
            // Insert new
            String insert = "INSERT INTO " + TABLE + " (process_id, parent_id, agent_name, status, content_type, payload, version, created_at, updated_at) "
                    + "VALUES (:process_id, :parent_id, :agent_name, :status, :content_type, :payload, :version, :created_at, :updated_at)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("process_id", id)
                    .addValue("parent_id", snapshot.getParentId())
                    .addValue("agent_name", snapshot.getAgentName())
                    .addValue("status", snapshot.getStatus())
                    .addValue("content_type", snapshot.getContentType())
                    .addValue("payload", snapshot.getPayload())
                    .addValue("version", snapshot.getVersion())
                    .addValue("created_at", snapshot.getCreatedAt())
                    .addValue("updated_at", snapshot.getUpdatedAt());
            try {
                jdbc.update(insert, params);
            } catch (DuplicateKeyException ex) {
                // Another writer inserted concurrently - fall through to update path
                upsertUpdate(snapshot, now);
            }
        } else {
            upsertUpdate(snapshot, now);
        }
    }

    private void upsertUpdate(AgentProcessSnapshot snapshot, Instant now) {
        long expected = snapshot.getVersion();
        long newVersion = expected + 1;
        String update = "UPDATE " + TABLE + " SET parent_id = :parent_id, agent_name = :agent_name, status = :status, content_type = :content_type, payload = :payload, version = :new_version, updated_at = :updated_at "
                + "WHERE process_id = :process_id AND version = :expected_version";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("parent_id", snapshot.getParentId())
                .addValue("agent_name", snapshot.getAgentName())
                .addValue("status", snapshot.getStatus())
                .addValue("content_type", snapshot.getContentType())
                .addValue("payload", snapshot.getPayload())
                .addValue("new_version", newVersion)
                .addValue("updated_at", now)
                .addValue("process_id", snapshot.getProcessId())
                .addValue("expected_version", expected);

        int rows = jdbc.update(update, params);
        if (rows == 0) {
            throw new OptimisticLockingFailureException("Failed to update snapshot - version conflict for processId=" + snapshot.getProcessId());
        }
    }

    @Override
    public Optional<AgentProcessSnapshot> load(UUID processId) {
        String sql = "SELECT process_id, parent_id, agent_name, status, content_type, payload, version, created_at, updated_at "
                + "FROM " + TABLE + " WHERE process_id = :process_id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("process_id", processId);
        List<AgentProcessSnapshot> rows = jdbc.query(sql, params, new SnapshotRowMapper());
        if (rows.isEmpty()) return Optional.empty();
        return Optional.of(rows.get(0));
    }

    @Override
    public void delete(UUID processId) {
        String sql = "DELETE FROM " + TABLE + " WHERE process_id = :process_id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("process_id", processId);
        jdbc.update(sql, params);
    }

    @Override
    public List<AgentProcessSnapshot> findByParentId(UUID parentId) {
        if (parentId == null) return List.of();
        String sql = "SELECT process_id, parent_id, agent_name, status, content_type, payload, version, created_at, updated_at "
                + "FROM " + TABLE + " WHERE parent_id = :parent_id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("parent_id", parentId);
        return jdbc.query(sql, params, new SnapshotRowMapper());
    }

    @Override
    public List<AgentProcessSnapshot> findByAgentName(String agentName) {
        if (agentName == null) return List.of();
        String sql = "SELECT process_id, parent_id, agent_name, status, content_type, payload, version, created_at, updated_at "
                + "FROM " + TABLE + " WHERE agent_name = :agent_name";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("agent_name", agentName);
        return jdbc.query(sql, params, new SnapshotRowMapper());
    }

    private static class SnapshotRowMapper implements RowMapper<AgentProcessSnapshot> {
        @Override
        public AgentProcessSnapshot mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID processId = rs.getObject("process_id", UUID.class);
            UUID parentId = rs.getObject("parent_id", UUID.class);
            String agentName = rs.getString("agent_name");
            String status = rs.getString("status");
            String contentType = rs.getString("content_type");
            String payload = rs.getString("payload");
            long version = rs.getLong("version");
            Instant createdAt = rs.getTimestamp("created_at").toInstant();
            Instant updatedAt = rs.getTimestamp("updated_at").toInstant();
            return new AgentProcessSnapshot(processId, parentId, agentName, status, contentType, payload, version, createdAt, updatedAt);
        }
    }
}
