package com.infinity.julien.auth.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record SignupRequest(
        @Email
        String email,

        Set<String> roles,

        @NotBlank
        @Size(min = 6, max = 20)
        String password,

        @NotBlank
        String organisation
) {
}