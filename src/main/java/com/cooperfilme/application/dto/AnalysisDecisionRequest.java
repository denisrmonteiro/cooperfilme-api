package com.cooperfilme.application.dto;

import jakarta.validation.constraints.*;

public record AnalysisDecisionRequest(
    @NotBlank String justification,
    @NotNull Boolean sendToReview
) {}