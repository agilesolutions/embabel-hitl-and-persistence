package com.agilesolutions.embabel.agent;

import com.agilesolutions.embabel.model.DraftPost;
import com.agilesolutions.embabel.model.HumanReview;
import com.agilesolutions.embabel.model.QuoteResponse;
import com.agilesolutions.embabel.model.ReviewedResult;
import com.agilesolutions.embabel.prompt.Personas;
import com.agilesolutions.embabel.tool.FinancialAssetTool;
import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.Ai;
import com.embabel.agent.core.hitl.WaitFor;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.common.ai.model.LlmOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Agent(description = "This agent is responsible for retrieving financial asset information from the Twelve Data API.")
public class AssetAgent {

    private final FinancialAssetTool financialAssetTool;

    @Action(description = "Retrieve financial asset information for a given symbol.")
    public QuoteResponse retrieveAssetInformation(UserInput userInput, Ai ai) {

        QuoteResponse response = ai
        .withDefaultLlm()
        .withToolObject(financialAssetTool)
        .withId("financial-asset-tool")
                .withPromptContributors(List.of(Personas.REVIEWER))
                .creating(QuoteResponse.class)
                .fromPrompt("Retrieve the financial asset information for the following symbol: " + userInput.getContent());

        log.info("Retrieved financial asset information: {}", response);

        return response;

    }

    @Action
    public ReviewedResult verifyLargeTransaction(QuoteResponse tx) {
        // Autonomous guardrail check
        if (tx.close() < 10000) {
            return new ReviewedResult(tx.name(), tx.currency(), tx.close().toString(), true, "Auto-approved by policy.");
        }

        // Trigger Human-in-the-Loop workflow for high-value transfers
        HumanReview review = WaitFor.formSubmission(
                "Transaction close value is above $10,000. Please review the transaction details and approve or reject.",
                HumanReview.class
        );

        if (!review.approved()) {
            return new ReviewedResult(tx.name(), tx.currency(), tx.close().toString(), false, "Rejected by compliance: " + review.correctionNotes());
        }

        return new ReviewedResult(tx.name(), tx.currency(), tx.close().toString(), true, "Manually approved by human operator.");
    }

    @AchievesGoal(description = "Produce a blog post about the financial asset based on the reviewed information.")
    @Action(description = "Write a first draft of the blog post")
    public DraftPost generatePost(ReviewedResult reviewed, Ai ai) {
        return ai
                .withLlm(LlmOptions.withDefaults().withMaxTokens(16384))
                .withId("financial-blog-post-draft")
                .withPromptContributors(List.of(Personas.WRITER, Personas.JSON_OUTPUT))
                .creating(DraftPost.class)
                .fromPrompt("""
                        Write a blog post about: %s

                        Use the following research to inform your writing:
                        %s

                        Use short sentences and plain language.
                        Write the content in Markdown.
                        """.formatted(reviewed.name(), reviewed.description())
                );
    }

}
