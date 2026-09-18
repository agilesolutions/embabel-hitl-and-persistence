package com.agilesolutions.embabel.persistence;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple thread-safe in-memory implementation of {@link AgentProcessSnapshotStore}.
 * This is intended for local development and tests. It stores snapshots in a
 * ConcurrentHashMap keyed by processId and supports simple find operations.
 */
public class InMemoryAgentProcessSnapshotStore implements AgentProcessSnapshotStore {

    private final Map<UUID, AgentProcessSnapshot> store = new ConcurrentHashMap<>();

    @Override
    public void save(AgentProcessSnapshot snapshot) {
        // Very small optimistic-lock-like check: if an existing snapshot exists and versions differ
        // the store will replace it unconditionally. Implementations should enforce stronger
        // semantics if required (throw on version conflict).
        final UUID id = snapshot.getProcessId();
        store.put(id, snapshot);
    }

    @Override
    public Optional<AgentProcessSnapshot> load(UUID processId) {
        return Optional.ofNullable(store.get(processId));
    }

    @Override
    public void delete(UUID processId) {
        store.remove(processId);
    }

    @Override
    public List<AgentProcessSnapshot> findByParentId(UUID parentId) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        List<AgentProcessSnapshot> result = new ArrayList<>();
        for (AgentProcessSnapshot s : store.values()) {
            if (parentId.equals(s.getParentId())) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public List<AgentProcessSnapshot> findByAgentName(String agentName) {
        if (agentName == null) {
            return Collections.emptyList();
        }
        List<AgentProcessSnapshot> result = new ArrayList<>();
        for (AgentProcessSnapshot s : store.values()) {
            if (agentName.equals(s.getAgentName())) {
                result.add(s);
            }
        }
        return result;
    }

    // Convenience factory method for tests
    public AgentProcessSnapshot createSnapshot(UUID processId,
                                               UUID parentId,
                                               String agentName,
                                               String status,
                                               String contentType,
                                               String payload) {
        Instant now = Instant.now();
        AgentProcessSnapshot snapshot = new AgentProcessSnapshot(processId, parentId, agentName, status, contentType, payload, 1L, now, now);
        save(snapshot);
        return snapshot;
    }
}
