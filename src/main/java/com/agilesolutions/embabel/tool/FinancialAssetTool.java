package com.agilesolutions.embabel.tool;

import com.agilesolutions.embabel.model.QuoteResponse;
import com.embabel.agent.api.annotation.LlmTool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class FinancialAssetTool {

    private final RestClient restClient;

    @Value("${twelvedata.api-key}")
    private final String apiKey;

        @LlmTool(description = "Calculate word count and estimated reading time (in minutes) for a piece of text. Reading speed is assumed to be 200 words per minute.")
        public QuoteResponse calculateReadingStats(
                @LlmTool.Param(description = "The full asset symbol to retrieving this product") String symbol) {

            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/quote")
                            .queryParam("symbol", symbol)
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .body(QuoteResponse.class);

    }

}

