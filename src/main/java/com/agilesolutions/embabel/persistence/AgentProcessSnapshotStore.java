package com.agilesolutions.embabel.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence SPI for agent process snapshots.
 *
 * Implementations are expected to store and retrieve serialized snapshots of
 * an Embabel AgentProcess (including the blackboard payload). The framework
 * will perform serialization/deserialization of blackboard entries. The
 * store is responsible for durable storage and optimistic versioning semantics.
 */
public interface AgentProcessSnapshotStore {

    /** Persist or update the given snapshot. Implementations should use optimistic
     *  semantics if they support versioning (i.e. fail or throw on version conflict).
     */
    void save(AgentProcessSnapshot snapshot);

    /** Load a snapshot by process id. */
    Optional<AgentProcessSnapshot> load(UUID processId);

    /** Delete a snapshot by process id. */
    void delete(UUID processId);

    /** Find snapshots that have the given parent id. */
    List<AgentProcessSnapshot> findByParentId(UUID parentId);

    /** Find snapshots by agent name. */
    List<AgentProcessSnapshot> findByAgentName(String agentName);

}
