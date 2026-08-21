package com.agilesolutions.embabel.model;

public record ReviewedResult(
        String name,
        String currency,
        String close,
        boolean approved,
        String description
) { }