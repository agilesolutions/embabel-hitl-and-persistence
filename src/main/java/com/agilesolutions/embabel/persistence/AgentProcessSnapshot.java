package com.agilesolutions.embabel.persistence;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Simple immutable snapshot container for persistence implementations.
 * payload is stored as a String (usually JSON) and contentType describes the
 * serialized format (eg. "application/json"). Implementations may extend or
 * store arbitrary bytes as needed.
 */
public final class AgentProcessSnapshot {

    private final UUID processId;
    private final UUID parentId;
    private final String agentName;
    private final String status;
    private final String contentType;
    private final String payload;
    private final long version;
    private final Instant createdAt;
    private final Instant updatedAt;

    public AgentProcessSnapshot(UUID processId,
                                UUID parentId,
                                String agentName,
                                String status,
                                String contentType,
                                String payload,
                                long version,
                                Instant createdAt,
                                Instant updatedAt) {
        this.processId = Objects.requireNonNull(processId, "processId");
        this.parentId = parentId;
        this.agentName = Objects.requireNonNull(agentName, "agentName");
        this.status = Objects.requireNonNull(status, "status");
        this.contentType = Objects.requireNonNull(contentType, "contentType");
        this.payload = Objects.requireNonNull(payload, "payload");
        this.version = version;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public UUID getProcessId() {
        return processId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getStatus() {
        return status;
    }

    public String getContentType() {
        return contentType;
    }

    public String getPayload() {
        return payload;
    }

    public long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public AgentProcessSnapshot withVersion(long newVersion, Instant newUpdatedAt) {
        return new AgentProcessSnapshot(processId, parentId, agentName, status, contentType, payload, newVersion, createdAt, newUpdatedAt);
    }

}
