package com.infinity.julien.security.jwt;

import com.infinity.julien.payloads.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthEntryPointJWT implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJWT.class);

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        // Log the error
        logger.error("Unauthorized error: {}", authException.getMessage());
        // Log the error
        logger.error("Unauthorized error: {}", (Object) authException.getStackTrace());
        // Set response status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Create a custom JSON error message
        String errorResponse = "{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}";

        // Write the error response to the response writer
        response.getWriter().write(ApiResponse.error(authException.getMessage()).toString());
    }
}
