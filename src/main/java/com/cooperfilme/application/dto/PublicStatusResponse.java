package com.cooperfilme.application.dto;

public record PublicStatusResponse(
    Long id,
    String status,
    String lastUpdate
) {}