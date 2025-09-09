package com.cooperfilme.application.dto;

import jakarta.validation.constraints.NotNull;

public record VoteRequest(
    @NotNull Boolean approved,
    String note
) {}