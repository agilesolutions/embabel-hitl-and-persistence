package com.agilesolutions.embabel.persistence;

import com.embabel.agent.api.common.PlannerType;

public record ProcessOptionsSnapshot(
        String contextId,
        PlannerType plannerType,
        boolean prune,
        boolean ephemeral
) {
}