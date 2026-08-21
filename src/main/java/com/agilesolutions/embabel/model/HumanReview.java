package com.agilesolutions.embabel.model;

import org.springframework.context.annotation.Description;

public record HumanReview(
        @Description("Whether the generated response is safe and accurate to send to the client.")
        boolean approved,

        @Description("Detailed feedback or corrections if rejected.")
        String correctionNotes
) {}