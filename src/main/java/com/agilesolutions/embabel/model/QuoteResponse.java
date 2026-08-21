package com.agilesolutions.embabel.model;

public record QuoteResponse(
        String symbol,
        String name,
        String exchange,
        String currency,
        Double close,
        String datetime
) { }