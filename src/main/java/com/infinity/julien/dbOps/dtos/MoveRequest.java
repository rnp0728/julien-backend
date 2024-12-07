package com.infinity.julien.dbOps.dtos;

import jakarta.validation.constraints.NotBlank;

public record MoveRequest(
        @NotBlank
        String sourceEnvId,
        @NotBlank
        String destinationEnvId,
        @NotBlank
        String collectionName,
        boolean clearExistingData
) {
}
