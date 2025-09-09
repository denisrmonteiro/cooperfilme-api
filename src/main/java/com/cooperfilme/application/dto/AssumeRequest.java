package com.cooperfilme.application.dto;

import jakarta.validation.constraints.NotNull;

public record AssumeRequest(
    @NotNull Long userId
) {}