package com.infinity.julien.environment.payload;

import jakarta.validation.constraints.NotBlank;

public record EnvironmentCreationRequest(
        @NotBlank
        String name,
        @NotBlank
        String project,
        @NotBlank
        String dbUrl,
        @NotBlank
        String dbUser,
        @NotBlank
        String dbPassword,
        @NotBlank
        String dbName
) {
}
