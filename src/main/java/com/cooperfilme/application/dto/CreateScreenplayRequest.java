package com.cooperfilme.application.dto;

import jakarta.validation.constraints.*;

public record CreateScreenplayRequest(
    @NotBlank String text,
    @NotBlank String clientName,
    @Email @NotBlank String clientEmail,
    @NotBlank String clientPhone
) {}