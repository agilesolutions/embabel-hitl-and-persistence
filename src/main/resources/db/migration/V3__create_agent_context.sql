CREATE TABLE agent_context
(
    id              UUID PRIMARY KEY,
    state           JSONB NOT NULL DEFAULT '{}'::jsonb,
    version         BIGINT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_agent_context_version
        CHECK (version >= 0)
);

CREATE INDEX idx_agent_context_updated_at
    ON agent_context (updated_at);