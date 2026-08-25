CREATE TABLE agent_process
(
    id              UUID PRIMARY KEY,
    agent_name      VARCHAR(255) NOT NULL,
    status          VARCHAR(50) NOT NULL,
    current_action  VARCHAR(255),
    state           JSONB NOT NULL DEFAULT '{}'::jsonb,
    version         BIGINT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_agent_process_version
        CHECK (version >= 0)
);

CREATE INDEX idx_agent_process_status
    ON agent_process (status);

CREATE INDEX idx_agent_process_agent_name
    ON agent_process (agent_name);

CREATE INDEX idx_agent_process_updated_at
    ON agent_process (updated_at);