package com.infinity.julien.auth.payloads;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SigninRequest(
        @Email
        String email,

        @NotBlank
        String password
) {
}
