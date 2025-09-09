package com.cooperfilme.application.dto;

public record LoginResponse(
    String token,
    String name,
    String role
) {}

