package com.infinity.julien.auth;

import com.infinity.julien.auth.payloads.RefreshRequest;
import com.infinity.julien.auth.payloads.SigninRequest;
import com.infinity.julien.auth.payloads.SignupRequest;
import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.payloads.ApiResponse;
import com.infinity.julien.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signUpRequest) throws NotFoundException {
        if (userService.existsByUsername(signUpRequest.email())) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Username is already taken!"));
        }
        authService.createUser(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest signinRequest) throws NotFoundException {
        if (!userService.existsByUsername(signinRequest.email())) {
            throw new NotFoundException("Email not found!");
        }

        return ResponseEntity.ok(authService.authenticateUser(signinRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest refreshRequest)
            throws NotFoundException {
        return ResponseEntity.ok(authService.refreshToken(refreshRequest));
    }
}
