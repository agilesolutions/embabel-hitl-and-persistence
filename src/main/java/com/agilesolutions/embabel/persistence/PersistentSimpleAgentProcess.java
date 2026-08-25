package com.agilesolutions.embabel.persistence;

import com.embabel.agent.api.common.PlatformServices;
import com.embabel.agent.core.Agent;
import com.embabel.agent.core.AgentProcessStatusCode;
import com.embabel.agent.core.Blackboard;
import com.embabel.agent.core.ProcessOptions;
import com.embabel.agent.core.support.SimpleAgentProcess;
import com.embabel.agent.spi.PlannerFactory;

import java.time.Instant;

public class PersistentSimpleAgentProcess
        extends SimpleAgentProcess {

    public PersistentSimpleAgentProcess(
            String id,
            String parentId,
            Agent agent,
            ProcessOptions processOptions,
            Blackboard blackboard,
            PlatformServices platformServices,
            PlannerFactory plannerFactory,
            Instant timestamp) {

        super(
                id,
                parentId,
                agent,
                processOptions,
                blackboard,
                platformServices,
                plannerFactory,
                timestamp
        );
    }

    public void restoreStatus(
            AgentProcessStatusCode status) {

        setStatus(status);
    }
}