package com.agilesolutions.embabel.repository;

import com.agilesolutions.embabel.persistence.*;
import com.embabel.agent.api.common.PlannerType;
import com.embabel.agent.core.AbstractAgentProcessRepository;
import com.embabel.agent.core.Agent;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.core.AgentProcess;
import com.embabel.agent.core.AgentProcessStatusCode;
import com.embabel.agent.core.Blackboard;
import com.embabel.agent.core.ContextId;
import com.embabel.agent.core.ProcessOptions;
import com.embabel.agent.core.support.InMemoryBlackboard;
import com.embabel.agent.spi.support.DefaultPlannerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Repository
public class PostgresAgentProcessRepository
        extends AbstractAgentProcessRepository {

    private final JdbcTemplate jdbc;
    private final ObjectMapper objectMapper;
    private final ObjectProvider<AgentPlatform> platformProvider;

    public PostgresAgentProcessRepository(
            JdbcTemplate jdbc,
            ObjectMapper objectMapper,
            ObjectProvider<AgentPlatform> platformProvider) {

        this.jdbc = jdbc;
        this.objectMapper = objectMapper;
        this.platformProvider = platformProvider;
    }

    @Override
    public AgentProcess findById(String id) {

        List<ProcessSnapshot> snapshots =
                jdbc.query(
                        """
                        SELECT
                            id,
                            agent_name,
                            parent_id,
                            context_id,
                            status,
                            created_at,
                            blackboard,
                            process_options
                        FROM agent_process
                        WHERE id = ?
                        """,
                        (rs, rowNum) -> {

                            try {

                                BlackboardSnapshot blackboard =
                                        objectMapper.readValue(
                                                rs.getString("blackboard"),
                                                BlackboardSnapshot.class
                                        );

                                ProcessOptionsSnapshot options =
                                        objectMapper.readValue(
                                                rs.getString("process_options"),
                                                ProcessOptionsSnapshot.class
                                        );

                                return new ProcessSnapshot(
                                        rs.getString("id"),
                                        rs.getString("agent_name"),
                                        rs.getString("parent_id"),
                                        rs.getString("context_id"),
                                        rs.getString("status"),
                                        rs.getTimestamp("created_at")
                                                .toInstant(),
                                        blackboard,
                                        options
                                );

                            }
                            catch (Exception e) {

                                throw new IllegalStateException(
                                        "Unable to read agent process "
                                                + rs.getString("id"),
                                        e
                                );
                            }
                        },
                        id
                );

        if (snapshots.isEmpty()) {
            return null;
        }

        return restore(snapshots.getFirst());
    }

    @Override
    public List<AgentProcess> findByParentId(
            String parentId) {

        return jdbc.query(
                """
                SELECT id
                FROM agent_process
                WHERE parent_id = ?
                ORDER BY created_at
                """,
                (rs, rowNum) ->
                        findById(rs.getString("id")),
                parentId
        );
    }

    @Override
    @Transactional
    protected AgentProcess doSave(
            AgentProcess agentProcess) {

        String blackboardJson =
                serializeBlackboard(
                        agentProcess.getBlackboard()
                );

        String optionsJson =
                serializeOptions(
                        agentProcess.getProcessOptions()
                );

        jdbc.update(
                """
                INSERT INTO agent_process
                (
                    id,
                    agent_name,
                    parent_id,
                    context_id,
                    status,
                    created_at,
                    updated_at,
                    blackboard,
                    process_options,
                    version
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    CURRENT_TIMESTAMP,
                    ?::jsonb,
                    ?::jsonb,
                    0
                )
                """,
                agentProcess.getId(),
                agentProcess.getAgent().getName(),
                agentProcess.getParentId(),
                agentProcess.getProcessOptions()
                        .getContextIdString(),
                agentProcess.getStatus().name(),
                java.sql.Timestamp.from(
                        agentProcess.getTimestamp()
                ),
                blackboardJson,
                optionsJson
        );

        return agentProcess;
    }

    @Override
    @Transactional
    protected void doUpdate(
            AgentProcess agentProcess) {

        String blackboardJson =
                serializeBlackboard(
                        agentProcess.getBlackboard()
                );

        String optionsJson =
                serializeOptions(
                        agentProcess.getProcessOptions()
                );

        /*
         * The version is maintained by PostgreSQL.
         *
         * For the first implementation we use the process timestamp
         * as the optimistic-lock boundary only through a dedicated
         * database row lock.
         *
         * The stronger versioned implementation is shown below.
         */
        int updated =
                jdbc.update(
                        """
                        UPDATE agent_process
                        SET
                            status = ?,
                            blackboard = ?::jsonb,
                            process_options = ?::jsonb,
                            updated_at = CURRENT_TIMESTAMP,
                            version = version + 1
                        WHERE id = ?
                        """,
                        agentProcess.getStatus().name(),
                        blackboardJson,
                        optionsJson,
                        agentProcess.getId()
                );

        if (updated != 1) {

            throw new ProcessOptimisticLockException(
                    "Agent process disappeared: "
                            + agentProcess.getId()
            );
        }
    }

    @Override
    @Transactional
    public void delete(
            AgentProcess agentProcess) {

        jdbc.update(
                """
                DELETE FROM agent_process
                WHERE id = ?
                """,
                agentProcess.getId()
        );
    }

    private AgentProcess restore(
            ProcessSnapshot snapshot) {

        AgentPlatform platform =
                platformProvider.getObject();

        Agent agent =
                platform.agents()
                        .stream()
                        .filter(a ->
                                a.getName()
                                        .equals(snapshot.agentName()))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Agent '" +
                                                snapshot.agentName() +
                                                "' is not deployed"
                                )
                        );

        Blackboard blackboard =
                restoreBlackboard(
                        snapshot.blackboard()
                );

        ProcessOptions options =
                restoreProcessOptions(
                        snapshot.processOptions()
                ).withBlackboard(blackboard);

        PersistentSimpleAgentProcess process =
                new PersistentSimpleAgentProcess(
                        snapshot.id(),
                        snapshot.parentId(),
                        agent,
                        options,
                        blackboard,
                        platform.getPlatformServices(),
                        DefaultPlannerFactory.INSTANCE,
                        snapshot.timestamp()
                );

        process.restoreStatus(
                AgentProcessStatusCode.valueOf(
                        snapshot.status()
                )
        );

        return process;
    }

    private Blackboard restoreBlackboard(
            BlackboardSnapshot snapshot) {

        InMemoryBlackboard blackboard =
                new InMemoryBlackboard();

        if (snapshot.bindings() != null) {

            snapshot.bindings()
                    .forEach(
                            blackboard::bind
                    );
        }

        if (snapshot.objects() != null) {

            for (Object object :
                    snapshot.objects()) {

                if (snapshot.bindings() == null ||
                        !snapshot.bindings()
                                .containsValue(object)) {

                    blackboard.addObject(object);
                }
            }
        }

        return blackboard;
    }

    private ProcessOptions restoreProcessOptions(
            ProcessOptionsSnapshot snapshot) {

        ProcessOptions options =
                new ProcessOptions()
                        .withPlannerType(
                                snapshot.plannerType()
                        )
                        .withPrune(
                                snapshot.prune()
                        )
                        .withEphemeral(
                                snapshot.ephemeral()
                        );

        if (snapshot.contextId() != null) {

            options = options.withContextId(snapshot.contextId());
        }

        return options;
    }

    private String serializeBlackboard(
            Blackboard blackboard) {

        return serialize(
                new BlackboardSnapshot(
                        blackboard.expressionEvaluationModel(),
                        blackboard.getObjects()
                )
        );
    }

    private String serializeOptions(
            ProcessOptions options) {

        ProcessOptionsSnapshot snapshot =
                new ProcessOptionsSnapshot(
                        options.getContextIdString(),
                        options.getPlannerType(),
                        options.getPrune(),
                        options.getEphemeral()
                );

        return serialize(snapshot);
    }

    private String serialize(
            Object value) {

        try {
            return objectMapper.writeValueAsString(
                    value
            );
        }
        catch (JsonProcessingException e) {

            throw new IllegalStateException(
                    "Unable to serialize Embabel state",
                    e
            );
        }
    }
}
