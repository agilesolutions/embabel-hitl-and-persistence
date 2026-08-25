CREATE TABLE agent_blackboard
(
    process_id      UUID PRIMARY KEY,
    state           JSONB NOT NULL DEFAULT '{}'::jsonb,
    version         BIGINT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_agent_blackboard_process
        FOREIGN KEY (process_id)
            REFERENCES agent_process (id)
            ON DELETE CASCADE,

    CONSTRAINT chk_agent_blackboard_version
        CHECK (version >= 0)
);