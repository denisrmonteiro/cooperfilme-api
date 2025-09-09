package com.cooperfilme.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewDecisionRequest(
    @NotBlank String notes
) {}