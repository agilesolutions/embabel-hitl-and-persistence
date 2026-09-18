package com.agilesolutions.embabel.persistence;

import com.agilesolutions.embabel.TestcontainersConfiguration;
import com.agilesolutions.embabel.EmbabelApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = {PostgresPersistenceConfiguration.class, PostgresAgentProcessSnapshotStore.class, TestcontainersConfiguration.class})
class PostgresAgentProcessSnapshotStoreIT {

    @Autowired
    PostgresAgentProcessSnapshotStore store;

    @BeforeEach
    void setUp() {
        // no-op
    }

    @AfterEach
    void tearDown() {
        // cleanup any leftover rows for safety
    }

    @Test
    void insertAndLoad() {
        UUID id = UUID.randomUUID();
        UUID parent = UUID.randomUUID();
        Instant now = Instant.now();
        AgentProcessSnapshot snap = new AgentProcessSnapshot(id, parent, "asset-agent", "WAITING", "application/json", "{\"k\":\"v\"}", 1L, now, now);

        store.save(snap);

        Optional<AgentProcessSnapshot> loaded = store.load(id);
        assertTrue(loaded.isPresent(), "snapshot should be present after save");
        AgentProcessSnapshot s = loaded.get();
        assertEquals(id, s.getProcessId());
        assertEquals(parent, s.getParentId());
        assertEquals("asset-agent", s.getAgentName());
        assertEquals(1L, s.getVersion());
        assertEquals("{\"k\":\"v\"}", s.getPayload());
    }

    @Test
    void upsertAndOptimisticLocking() {
        UUID id = UUID.randomUUID();
        UUID parent = UUID.randomUUID();
        Instant now = Instant.now();
        AgentProcessSnapshot initial = new AgentProcessSnapshot(id, parent, "asset-agent", "WAITING", "application/json", "{\"v\":1}", 1L, now, now);
        store.save(initial);

        // update with expected version 1 -> should succeed and become version 2
        AgentProcessSnapshot update = new AgentProcessSnapshot(id, parent, "asset-agent", "WAITING", "application/json", "{\"v\":2}", 1L, now, Instant.now());
        store.save(update);

        AgentProcessSnapshot loaded = store.load(id).orElseThrow();
        assertEquals(2L, loaded.getVersion(), "version should be incremented to 2");
        assertEquals("{\"v\":2}", loaded.getPayload());

        // attempt to save a stale snapshot (still version 1) -> expect optimistic locking
        AgentProcessSnapshot stale = new AgentProcessSnapshot(id, parent, "asset-agent", "WAITING", "application/json", "{\"v\":stale}", 1L, now, Instant.now());
        assertThrows(OptimisticLockingFailureException.class, () -> store.save(stale));
    }

    @Test
    void findByParentAndAgentNameAndDelete() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID parent = UUID.randomUUID();
        Instant now = Instant.now();

        AgentProcessSnapshot s1 = new AgentProcessSnapshot(id1, parent, "agent-A", "WAITING", "application/json", "p1", 1L, now, now);
        AgentProcessSnapshot s2 = new AgentProcessSnapshot(id2, parent, "agent-A", "WAITING", "application/json", "p2", 1L, now, now);

        store.save(s1);
        store.save(s2);

        List<AgentProcessSnapshot> byParent = store.findByParentId(parent);
        assertTrue(byParent.size() >= 2, "should find snapshots by parent id");

        List<AgentProcessSnapshot> byAgent = store.findByAgentName("agent-A");
        assertTrue(byAgent.size() >= 2, "should find snapshots by agent name");

        // delete one and ensure it is removed
        store.delete(id1);
        assertTrue(store.load(id1).isEmpty(), "deleted snapshot should not be loadable");
    }
}
