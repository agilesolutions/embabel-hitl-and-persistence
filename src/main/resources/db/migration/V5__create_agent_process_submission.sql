CREATE TABLE agent_process_event
(
    id              UUID PRIMARY KEY,
    process_id      UUID NOT NULL,
    sequence        BIGINT NOT NULL,
    event_type      VARCHAR(255) NOT NULL,
    payload         JSONB,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_agent_process_event_process
        FOREIGN KEY (process_id)
            REFERENCES agent_process (id)
            ON DELETE CASCADE,

    CONSTRAINT uk_agent_process_event_sequence
        UNIQUE (process_id, sequence),

    CONSTRAINT chk_agent_process_event_sequence
        CHECK (sequence >= 0)
);

CREATE INDEX idx_agent_process_event_process
    ON agent_process_event (process_id);

CREATE INDEX idx_agent_process_event_type
    ON agent_process_event (event_type);

CREATE INDEX idx_agent_process_event_created_at
    ON agent_process_event (created_at);