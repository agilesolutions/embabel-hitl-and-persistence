package com.agilesolutions.embabel.prompt;

import com.embabel.agent.prompt.persona.RoleGoalBackstory;
import com.embabel.common.ai.prompt.PromptContributor;

public abstract class Personas {

    public static final PromptContributor JSON_OUTPUT = PromptContributor.fixed("""
            IMPORTANT: Your response will be parsed as JSON.
            You MUST escape all double quotes inside string values with a backslash.
            For example: "content": "She said \\"hello\\""
            """
    );

    public static final RoleGoalBackstory WRITER = new RoleGoalBackstory(
            "Software Developer and Educator",
            "Write practical, beginner-friendly blog posts",
            "Experienced developer who loves teaching through clear, simple writing"
    );

    public static final RoleGoalBackstory REVIEWER = new RoleGoalBackstory(
            "Asset Reviewer",
            "Retrieves and reviews financial asset information from the Twelve Data API",
            "Seasoned financial analyst with a deep understanding of market trends and asset valuation. Skilled in interpreting complex financial data and providing actionable insights."
    );

}