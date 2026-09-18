package com.agilesolutions.embabel.persistence;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.ByteArrayResource;

import javax.sql.DataSource;

@Configuration
public class PostgresPersistenceConfiguration {

    private static final String DDL = "CREATE TABLE IF NOT EXISTS agent_process_snapshot (" +
            "process_id UUID PRIMARY KEY,\n" +
            "parent_id UUID,\n" +
            "agent_name VARCHAR(255) NOT NULL,\n" +
            "status VARCHAR(64) NOT NULL,\n" +
            "content_type VARCHAR(255) NOT NULL,\n" +
            "payload JSONB NOT NULL,\n" +
            "version BIGINT NOT NULL,\n" +
            "created_at TIMESTAMP WITH TIME ZONE NOT NULL,\n" +
            "updated_at TIMESTAMP WITH TIME ZONE NOT NULL\n" +
            ");\n" +
            "CREATE INDEX IF NOT EXISTS idx_agent_process_snapshot_parent ON agent_process_snapshot(parent_id);\n" +
            "CREATE INDEX IF NOT EXISTS idx_agent_process_snapshot_status ON agent_process_snapshot(status);\n" +
            "CREATE INDEX IF NOT EXISTS idx_agent_process_snapshot_agent ON agent_process_snapshot(agent_name);";

    @Bean
    @ConditionalOnBean(DataSource.class)
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource ds) {
        return new NamedParameterJdbcTemplate(ds);
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public PostgresAgentProcessSnapshotStore postgresAgentProcessSnapshotStore(NamedParameterJdbcTemplate jdbc, DataSource ds) {
        // Ensure schema exists
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ByteArrayResource(DDL.getBytes()));
        populator.execute(ds);
        return new PostgresAgentProcessSnapshotStore(jdbc);
    }
}
