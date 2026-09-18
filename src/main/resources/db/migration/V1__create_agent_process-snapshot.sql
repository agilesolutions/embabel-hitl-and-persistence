CREATE TABLE agent_process_snapshot (
                                        process_id      VARCHAR(255) PRIMARY KEY,
                                        parent_id       VARCHAR(255),
                                        agent_name      VARCHAR(255) NOT NULL,
                                        status          VARCHAR(64) NOT NULL,
                                        content_type    VARCHAR(255) NOT NULL,
                                        payload         JSONB NOT NULL,
                                        version         BIGINT NOT NULL,
                                        created_at      TIMESTAMPTZ NOT NULL,
                                        updated_at      TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_agent_process_snapshot_parent
    ON agent_process_snapshot(parent_id);

CREATE INDEX idx_agent_process_snapshot_status
    ON agent_process_snapshot(status);