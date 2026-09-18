create table agent_process_snapshot
(
    process_id          varchar(255) primary key,
    parent_process_id   varchar(255),
    agent_name          varchar(500) not null,
    status              varchar(50)  not null,
    content_type        varchar(255) not null,
    payload             jsonb        not null,
    version             bigint       not null,
    created_at          timestamptz  not null,
    updated_at          timestamptz  not null
);

create index idx_agent_process_snapshot_parent
    on agent_process_snapshot(parent_process_id);

create index idx_agent_process_snapshot_status
    on agent_process_snapshot(status);

create index idx_agent_process_snapshot_updated
    on agent_process_snapshot(updated_at);