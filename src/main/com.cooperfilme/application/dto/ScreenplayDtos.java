// application/dto/ScreenplayDtos.java
package com.cooperfilme.application.dto;

import jakarta.validation.constraints.*;

public record CreateScreenplayRequest(
    @NotBlank String text,
    @NotBlank String clientName,
    @Email @NotBlank String clientEmail,
    @NotBlank String clientPhone
) {}

public record ScreenplayFilter(
    String status, String email, String fromDate, String toDate
) {}

public record AssumeRequest(
    @NotNull Long userId
) {}

public record AnalysisDecisionRequest(
    @NotBlank String justification,
    @NotNull Boolean sendToReview // true -> AGUARDANDO_REVISAO; false -> RECUSADO
) {}

public record ReviewDecisionRequest(
    @NotBlank String notes
) {}

public record VoteRequest(
    @NotNull Boolean approved,
    String note
) {}

public record PublicStatusResponse(
    Long id, String status, String lastUpdate
) {}