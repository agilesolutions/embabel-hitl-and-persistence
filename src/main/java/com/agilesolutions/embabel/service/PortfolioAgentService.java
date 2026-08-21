package com.agilesolutions.embabel.service;

import com.agilesolutions.embabel.model.DraftPost;
import com.agilesolutions.embabel.model.PortfolioRefreshRequest;
import com.embabel.agent.api.invocation.AgentInvocation;
import com.embabel.agent.core.AgentPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioAgentService {

    private final AgentInvocation<PortfolioRefreshRequest> invocation;

    private final AgentPlatform agentPlatform;

    public DraftPost generateReport(
            PortfolioRefreshRequest request) {

        return AgentInvocation
                .create(
                        agentPlatform,
                        DraftPost.class)
                .invoke(request);
    }
}