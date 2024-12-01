package com.infinity.julien.auth.payloads;

import com.infinity.julien.user.User;
import jakarta.validation.constraints.NotBlank;

public record AuthenticatedResponse(
        @NotBlank
        String accessToken,
        @NotBlank
        String refreshToken,
        @NotBlank
        User user
) {
}
