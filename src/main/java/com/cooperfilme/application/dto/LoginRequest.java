package com.cooperfilme.application.dto;

import jakarta.validation.constraints.*;

public record LoginRequest(
    @Email @NotBlank String email,
    @NotBlank String password
) {}