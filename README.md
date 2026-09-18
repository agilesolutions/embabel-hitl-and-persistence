# Stateless Embabel Agent with PostgreSQL Persistence and HITL

This demo outlines a reference architecture for managing Human-in-the-Loop (HITL) workflows using **[Embabel](https://medium.com/@springrod/embabel-a-new-agent-platform-for-the-jvm-1c83402e0014)**. Avoiding in-memory state tracking, this design leverages a **PostgreSQL JDBC backend** for persistence, enabling horizontal scaling across multiple application instances. It incorporates **Optimistic Locking** to prevent race conditions, **Spring Data Envers**.

The workflow leverages an asynchronous **Embabel Agent** that executes automated steps, pauses at a `waitFor` human verification checkpoint, and safely commits execution snapshots to a central database cluster. Concurrency race conditions are safely resolved using **JPA Optimistic Locking (`@Version`)**, auditing is tracked via **Spring Data Envers**, API routing is fully guarded by **Spring Security OAuth2 JWT Resource Server** filters.

**Note**: This demo originally focused on persisting Blackboard worldstate snapshots on a PostgreSQL backend. The current design has been refactored to use a more generic Embabel Agent workflow, which can be adapted to various HITL scenarios.

## Current Embabel development line
As of **17 September 2026**, the repository has moved into a 2.0.0 development stream.

There is an important correction to my earlier setup: the persistence API is now real and documented in the current development stream. It is no longer merely a future concept.

The GitHub activity explicitly shows:

- branch: 2.0.0
- Start 2.0.0 Dev Stream
- Switch to embabel-common 2.0.0
- Update to dependency parent 2.0.0-SNAPSHOT

The project's release-branch documentation says that main **is reserved for the Spring 2.x development stream**, while 1.0.x is the maintenance/development branch for Spring AI 1.x.

---

## AgentProcessSnapshotStore is now implemented

The current documentation has a dedicated [§4.16 Persistence section](https://docs.embabel.com/embabel-agent/guide/1.5.2-SNAPSHOT/#reference.persistence).

The architecture is:

```
                    AgentProcess
                         │
                         ▼
              AgentProcessRepository
                         │
                 ┌───────┴────────┐
                 │                │
        Runtime repository   Snapshot Store
        (normally memory)        │
                 │                │
                 │          AgentProcessSnapshot
                 │                │
                 └────────┬───────┘
                          │
                     PostgreSQL
                     / Redis /
                       cache /
                    your backend
```
The key point is that **Embabel deliberately does not prescribe PostgreSQL**.

AgentProcessSnapshotStore is the persistence SPI, and your application supplies the actual storage implementation

## Important Classes
The current persistence architecture exposes these contracts/components:

### AgentProcessSnapshotStore
This is the principal persistence SPI.

It stores a serialized snapshot containing information including:

```
process id
parent id
agent name
status
content type
payload
timestamps
optimistic version
```

The framework remains backend-neutral.

Conceptually:
```
public interface AgentProcessSnapshotStore {

    // save snapshot

    // load snapshot

    // delete snapshot

    // find by process id

    // find by parent id
}
```
I would treat this as the application integration point.

### PersistentAgentProcessRepository
This is the default implementation of the `AgentProcessRepository` that uses a `AgentProcessSnapshotStore` to persist snapshots. This is the component that makes the snapshot store useful.

The current restore sequence is essentially:
```
lookup(processId)
       │
       ▼
runtime repository
       │
       ├── found ───────► return process
       │
       └── not found
              │
              ▼
      SnapshotStore
              │
              ▼
      deserialize snapshot
              │
              ▼
      reconstruct Blackboard
              │
              ▼
      reconstruct AgentProcess
              │
              ▼
      restore status/history
              │
              ▼
      runtime repository
              │
              ▼
          process
```

The documentation explicitly describes PersistentAgentProcessRepository performing this fallback from the runtime repository to the snapshot store.

This is the part that makes **pod-to-pod recovery** possible.

### AgentProcessPersistence
This is the component that orchestrates the persistence of snapshots. It is responsible for:
- saving snapshots to the store
- restoring snapshots from the store
- handling optimistic locking and versioning

The documentation gives the following pattern:
```
val repository =
    AgentProcessPersistence.persistentRepository(
        runtimeRepository = InMemoryAgentProcessRepository(),
        snapshotStore = mySnapshotStore,
        objectMapper = objectMapper,
        agents = agentPlatform::agents,
        platformServices = {
            agentPlatform.platformServices
        }
    )
```
So luckily I don't have to implement the process reconstruction myself. The framework owns the serialization/snapshot/restore machinery.

*This is a significant improvement over the architecture we discussed for Embabel 1.0.0.*

### AgentProcessCheckpointPolicy
The persistence SPI also includes a checkpoint policy that determines when to persist snapshots. The default implementation is `DefaultAgentProcessCheckpointPolicy`, which can be customized to suit specific application needs.

Currently there are two important policies:
```
WaitForCheckpointPolicy
        │
        └── checkpoint WAITING processes


LifecycleCheckpointPolicy
        │
        ├── WAITING
        └── finished processes
              ├── completed
              ├── failed
              ├── killed
              └── terminated
```

The configuration is:
```
embabel:
  agent:
    platform:
      persistence:
        enabled: true
        checkpoint-policy: LIFECYCLE
```
The default is currently LIFECYCLE.

### BlackboardEntrySerializer
This is the component responsible for serializing and deserializing the Blackboard entries. The default implementation uses Jackson for JSON serialization, but it can be customized to use other serialization formats if needed.

This is particularly important for the discussion around preserving World State.

The Blackboard is not simply dumped as one giant JSON object.

The persistence layer supports:
```
Blackboard
   │
   ├── Entry A
   │      ↓
   │   serializer
   │
   ├── Entry B
   │      ↓
   │   serializer
   │
   └── Entry C
          ↓
       serializer
```
Custom BlackboardEntrySerializer implementations are automatically discovered and take precedence over the fallback JSON serializer.

This is important for things like:

- JPA entities
- WaitFor/awaitables
- sensitive values
- runtime handles
- objects requiring stable identity

For example, instead of serializing:
```
@Entity
class Customer {
    ...
}
```
you could persist:
```
record CustomerReference(UUID id) {}
```
and resolve the entity again after restoration.

The documentation explicitly calls out JPA/Hibernate entities as a case where a custom serializer should be used.

### WorldState is still derived
This validates the architecture we discussed earlier.

The persisted object is primarily:
```
AgentProcess
     +
Blackboard
     +
process execution state
```
not:
```
AgentProcess
     +
Blackboard
     +
WorldState
     +
Plan
```
After restoration:
```
Persistent Snapshot
       │
       ▼
AgentProcess
       │
       ▼
Blackboard
       │
       ▼
BlackboardWorldStateDeterminer
       │
       ▼
WorldState
       │
       ▼
GOAP planner
       │
       ▼
new plan
```
So I am not going to persist the GOAP WorldState or plan as authoritative state.

That keeps recovery deterministic relative to the restored Blackboard and current agent definition.

### The really interesting part: current limitations
This is where your Kubernetes/PostgreSQL experiment becomes interesting.

The current documentation explicitly lists several limitations.

**Blackboard**
The current snapshot factory supports:
```
InMemoryBlackboard
```
and has an assumption that a WAITING process has exactly one pending awaitable.

Broader Blackboard support is expected to use a future BlackboardSnapshotter SPI.

**Transaction boundaries**
There are currently separate operations:
```
runtime repository write
          +
snapshot store write
```
The documentation notes that transaction-backed runtime/snapshot semantics require a persistent AgentProcessRepository using the same transaction boundary.

That's important for my PostgreSQL design.

**Process class names**
Snapshots currently use the JVM class name of the process implementation as an internal restore discriminator.

Therefore:
```
com.example.MyAgentProcess
```
→ rename class
```
com.example.CustomerAgentProcess
```
could invalidate existing snapshots without migration.

That's explicitly listed as a current limitation.

### Snapshot storage vs cache storage
There is another interesting development in the current implementation.

Embabel now has:
```
AgentProcessSnapshotStore
        │
        ▼
CacheBackedAgentProcessSnapshotStore
        │
        ▼
AgentCacheProvider
        │
        ▼
AgentCacheRegion
        │
        ▼
distributed cache
```

The cache-backed implementation is designed to provide durable agent processes without requiring a database implementation in the core framework.

It also uses **atomic compare-and-set** semantics for concurrent checkpoints.

That is highly relevant for:
```
Kubernetes
   │
   ├── Pod A
   ├── Pod B
   └── Pod C
         │
         ▼
    shared snapshot store
```

because multiple nodes may potentially attempt to checkpoint the same process.

### There is active work beyond the current implementation
his is perhaps the most useful discovery.

On **5 September 2026**, Embabel opened issue #2005, titled:

"Event-driven snapshot checkpointing with policy-based safety net".

The current implementation triggers persistence through:
```
PersistentAgentProcessRepository
        │
        ▼
save()/update()
        │
        ▼
LifecycleCheckpointPolicy
```
The proposed evolution is to checkpoint on semantic lifecycle events:
```
AgentProcessWaitingEvent
AgentProcessCompletedEvent
AgentProcessFailedEvent
ProcessKilledEvent
AgentProcessTerminatedEvent
```
and introduce:
```
AgentProcessRestoredEvent
```

The issue also proposes CAS-based deduplication of checkpoint writes.

So the persistence subsystem is **actively evolving right now**.

## What I will use now in this my projject
Given my earlier design and objective, I would now change my earlier architecture to:
```
                    Embabel 2.0 development
                              │
                              ▼
                       AgentProcess
                              │
                              ▼
                  PersistentAgentProcessRepository
                              │
                ┌─────────────┴──────────────┐
                │                            │
       InMemoryAgentProcessRepository   SnapshotStore
                                             │
                                             ▼
                                        PostgreSQL
                                             │
                                     JSON / JSONB
                                             │
                              ┌──────────────┴──────────────┐
                              │                             │
                       process metadata               Blackboard
                                                            │
                                                            ▼
                                             BlackboardEntrySerializer
                                                            │
                                                            ▼
                                             restore AgentProcess
                                                            │
                                                            ▼
                                           WorldState determined
                                                            │
                                                            ▼
                                                        Planner
```
And I would not implement a custom persistence framework anymore.

Instead, implement one adapter:
```
PostgresAgentProcessSnapshotStore
             implements
AgentProcessSnapshotStore
```
with PostgreSQL providing:
```
process_id       UUID
parent_id        UUID
agent_name       VARCHAR
status           VARCHAR
content_type     VARCHAR
payload          JSONB
version          BIGINT
created_at       TIMESTAMP
updated_at       TIMESTAMP
```
Then let Embabel own:
- snapshot creation
- Blackboard serialization
- process restoration
- World State reconstruction
- planning
- lifecycle management

My application owns:
- PostgreSQL
- transaction boundaries
- serialization of domain-specific Blackboard entries
- HA/deployment concerns

## One important version conclusion
There are now three different things that shouldn't be confused:
```
| Line                      | Status                                         | Persistence                         |
| ------------------------- | ---------------------------------------------- | ----------------------------------- |
| **1.0.x**                 | Spring AI 1.x line                             | older architecture                  |
| **1.5.2**                 | current/recent stable development/release line | **has `AgentProcessSnapshotStore`** |
| **2.0.0-SNAPSHOT / main** | **current future development**                 | persistence continues evolving      |
```
The repository milestone shows 1.5.2-Release is complete, while GitHub activity has already started the 2.0.0 development stream.

So for my stated goal — "I actually want to focus on the latest developments" — I'd use 2.0.0-SNAPSHOT/main, while treating the persistence SPI as an evolving API rather than assuming it is completely frozen.

The current version catalogue I already marked explicitly:
```
2.0.0-SNAPSHOT
Spring Boot 4.2.13.Final
```
which is a much better basis for the Spring Boot 4 + Kubernetes + PostgreSQL + Embabel experiment I have been struggling toward.

- [Embabel 2.0 development branch/activity](https://github.com/embabel/embabel-agent/activity)
- [Current Embabel persistence documentation](https://docs.embabel.com/embabel-agent/guide/1.5.3-SNAPSHOT/#reference.persistence)
- [Persistence checkpointing issue #2005](https://github.com/embabel/embabel-agent/issues/2005?utm_source=chatgpt.com)

## My specific implementation
So my architecture is now:
```
embabel-agent-starter
        │
        ├── AgentProcess
        ├── Blackboard
        ├── WorldState
        ├── AgentProcessPersistence
        ├── PersistentAgentProcessRepository
        ├── AgentProcessSnapshotStore
        └── BlackboardEntrySerializer
                     │
                     │ your implementation
                     ▼
             PostgreSQL / JDBC
```

### My PostgreSQL implementation
I will implement a `PostgresAgentProcessSnapshotStore` that implements the `AgentProcessSnapshotStore` interface. This will handle the persistence of snapshots in a PostgreSQL database, using JSONB for the payload and supporting optimistic locking with a version field.
```
src/main/java/
└── com/agilesolutions/embabel/persistence/
    ├── PostgresAgentProcessSnapshotStore.java
    ├── PostgresAgentProcessSnapshotRepository.java
    └── PostgresPersistenceConfiguration.java
```
My keyclass is:
```
@Component
public class PostgresAgentProcessSnapshotStore
        implements AgentProcessSnapshotStore {

    // JDBC implementation
}
```
And then Spring automatically detects it and uses it in the `PersistentAgentProcessRepository`.
```
embabel:
  agent:
    platform:
      persistence:
        enabled: true
        checkpoint-policy: LIFECYCLE
```
The documentation policies are:
```
WAITING
LIFECYCLE
``` 
WAITING is particularly interesting for your human-in-the-loop use case because it checkpoints processes parked at waitFor. LIFECYCLE additionally persists terminal states.

### PostgreSQL schema
I start for now with something like this:
```
CREATE TABLE agent_process_snapshot (
    process_id      UUID PRIMARY KEY,

    parent_id       UUID,

    agent_name      VARCHAR(255) NOT NULL,

    status          VARCHAR(64) NOT NULL,

    content_type    VARCHAR(255) NOT NULL,

    payload         JSONB NOT NULL,

    version         BIGINT NOT NULL,

    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_agent_process_snapshot_parent
    ON agent_process_snapshot(parent_id);

CREATE INDEX idx_agent_process_snapshot_status
    ON agent_process_snapshot(status);

CREATE INDEX idx_agent_process_snapshot_agent
    ON agent_process_snapshot(agent_name);
```

### TestContainers
I will use TestContainers to run a PostgreSQL instance for integration testing. This allows me to test the persistence layer in an isolated environment without requiring a separate database setup.

```
JUnit
  │
  ▼
PostgreSQL Testcontainer
  │
  ▼
Embabel Agent
  │
  ▼
AgentProcess
  │
  ▼
WAITING
  │
  ▼
PostgresAgentProcessSnapshotStore
  │
  ▼
PostgreSQL
  │
  │  destroy runtime process
  │
  ▼
restore
  │
  ▼
Blackboard
  │
  ▼
WorldState
  │
  ▼
resume
```


    
    


---


##  Referenties Nederlandse overheid
- [NDD exposure to DUO and me](docus/NDD.md)
- [NORA, ROSA and DUO](docus/nora.md)
