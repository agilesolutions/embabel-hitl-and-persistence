# Stateless Embabel Agent with PostgreSQL Persistence and HITL

This demo outlines a reference architecture for managing Human-in-the-Loop (HITL) workflows using **[Embabel](https://medium.com/@springrod/embabel-a-new-agent-platform-for-the-jvm-1c83402e0014)**. Avoiding in-memory state tracking, this design leverages a **PostgreSQL JDBC backend** for persistence, enabling horizontal scaling across multiple application instances. It incorporates **Optimistic Locking** to prevent race conditions, **Spring Data Envers**.

The workflow leverages an asynchronous **Embabel Agent** that executes automated steps, pauses at a `waitFor` human verification checkpoint, and safely commits execution snapshots to a central database cluster. Concurrency race conditions are safely resolved using **JPA Optimistic Locking (`@Version`)**, auditing is tracked via **Spring Data Envers**, API routing is fully guarded by **Spring Security OAuth2 JWT Resource Server** filters.

**Note**: This demo originally focused on persisting Blackboard worldstate snapshots on a PostgreSQL backend. The current design has been refactored to use a more generic Embabel Agent workflow, which can be adapted to various HITL scenarios.


---

## 1. System Topology & Architectural Map

```
               +----------------------------------------+
               |       API Gateway / Front-End UI       |
               +----------------------------------------+
                     |                            |
          [POST] /confirm                      [GET] /history
                     |                            |
                     v                            v
  +-----------------------------------------------------------------+
  |                    Spring Boot Application                      |
  |                                                                 |
  |  +-----------------------------------------------------------+  |
  |  |                 Security & Telemetry Tier                 |  |
  |  |  - Spring Security OAuth2 Filter (JWT Context Extraction) |  |
  |  |  - Global Exception Advice (Micrometer Alert Counting)    |  |
  |  +-----------------------------------------------------------+  |
  |                                |                                |
  |                                v                                |
  |  +-----------------------------------------------------------+  |
  |  |                    Business Logic Tier                    |  |
  |  |  - HitlTransactionController (REST Endpoints)             |  |
  |  |  - TransactionReviewService (Transactional Coordinator)   |  |
  |  |  - EmbabelAgentWorker (Asynchronous Engine Orchestrator)   |  |
  |  +-----------------------------------------------------------+  |
  |                 |                              |                |
  +-----------------|------------------------------|----------------+
                    | (JPA Data Operations)        | (JDBC Driver)
                    v                              v
  +-----------------------------------------------------------------+
  |                     PostgreSQL Database                         |
  |                                                                 |
  |   +--------------------------+    +-------------------------+   |
  |   |    Business Schema       |    |   Embabel Core State    |   |
  |   | - transactions           |    | - embabel_workflow_state|   |
  |   | - transactions_aud       |    +-------------------------+   |
  |   | - custom_revinfo         |                                  |
  |   +--------------------------+                                  |
  +-----------------------------------------------------------------+
```

---

##  Referenties Nederlandse overheid
- [NDD exposure to DUO and me](docus/NDD.md)
- [NORA, ROSA and DUO](docus/nora.md)
