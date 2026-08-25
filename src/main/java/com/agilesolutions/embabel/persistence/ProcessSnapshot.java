package com.agilesolutions.embabel.persistence;

import java.time.Instant;

public record ProcessSnapshot(
        String id,
        String agentName,
        String parentId,
        String contextId,
        String status,
        Instant timestamp,
        BlackboardSnapshot blackboard,
        ProcessOptionsSnapshot processOptions
) {
}