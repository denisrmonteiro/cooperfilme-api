package com.cooperfilme.application.dto;

public record ScreenplayFilter(
    String status,
    String email,
    String fromDate,
    String toDate
) {}