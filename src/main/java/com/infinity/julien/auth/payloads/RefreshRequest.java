package com.infinity.julien.auth.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefreshRequest(
        @NotBlank
        @NotNull
        String refreshToken,

        @NotBlank
        @NotNull
        String accessToken
) {
}
