package com.agilesolutions.embabel.repository;

import com.agilesolutions.embabel.persistence.BlackboardSnapshot;
import com.agilesolutions.embabel.persistence.PostgresContext;
import com.embabel.agent.core.Context;
import com.embabel.agent.spi.ContextRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class PostgresContextRepository
        implements ContextRepository {

    private final JdbcTemplate jdbc;
    private final ObjectMapper objectMapper;

    public PostgresContextRepository(
            JdbcTemplate jdbc,
            ObjectMapper objectMapper) {

        this.jdbc = jdbc;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Context create() {

        return createWithId(
                UUID.randomUUID().toString()
        );
    }

    @Override
    @Transactional
    public Context createWithId(String id) {

        PostgresContext context =
                new PostgresContext(id);

        save(context);

        return context;
    }

    @Override
    @Transactional
    public Context save(Context context) {

        PostgresContext postgresContext;

        if (context instanceof PostgresContext pc) {
            postgresContext = pc;
        }
        else {
            postgresContext = copy(context);
        }

        String json = serialize(
                new BlackboardSnapshot(
                        postgresContext.getBindings(),
                        postgresContext.getObjects()
                )
        );

        jdbc.update(
                """
                INSERT INTO agent_context
                (
                    id,
                    context,
                    version,
                    created_at,
                    updated_at
                )
                VALUES
                (
                    ?,
                    ?::jsonb,
                    0,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                )
                ON CONFLICT (id)
                DO UPDATE SET
                    context = EXCLUDED.context,
                    version = agent_context.version + 1,
                    updated_at = CURRENT_TIMESTAMP
                """,
                postgresContext.getId(),
                json
        );

        return postgresContext;
    }

    @Override
    public Context findById(String id) {

        List<String> result =
                jdbc.query(
                        """
                        SELECT context::text
                        FROM agent_context
                        WHERE id = ?
                        """,
                        (rs, rowNum) ->
                                rs.getString(1),
                        id
                );

        if (result.isEmpty()) {
            return null;
        }

        return deserialize(
                id,
                result.getFirst()
        );
    }

    @Override
    @Transactional
    public void delete(Context context) {

        jdbc.update(
                """
                DELETE FROM agent_context
                WHERE id = ?
                """,
                context.getId()
        );
    }

    private PostgresContext copy(Context context) {

        return new PostgresContext(
                context.getId(),
                Map.of(),
                context.getObjects()
        );
    }

    private String serialize(
            BlackboardSnapshot snapshot) {

        try {
            return objectMapper.writeValueAsString(snapshot);
        }
        catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Unable to serialize Embabel Context",
                    e
            );
        }
    }

    private PostgresContext deserialize(
            String id,
            String json) {

        try {

            JsonNode root =
                    objectMapper.readTree(json);

            Map<String, Object> bindings =
                    objectMapper.convertValue(
                            root.path("bindings"),
                            Map.class
                    );

            List<Object> objects =
                    objectMapper.convertValue(
                            root.path("objects"),
                            List.class
                    );

            return new PostgresContext(
                    id,
                    bindings,
                    objects
            );

        }
        catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to deserialize Embabel Context " + id,
                    e
            );
        }
    }
}