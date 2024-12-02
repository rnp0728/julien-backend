package com.infinity.julien.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Log request details.
        logRequestDetails(request);

        // Capture the start time.
        Instant startTime = Instant.now();

        try {
            filterChain.doFilter(request, response); // Proceed with the filter chain.
        } catch (Exception e) {
            logger.error("Exception during request processing", (Exception) e);
        } finally {
            // Measure processing time.
            Duration duration = Duration.between(startTime, Instant.now());

            // Log response details.
            logResponseDetails(response, request.getRequestURI(), duration);

            // Log any exceptions that occurred during request processing.
            Object exception = request.getAttribute("javax.servlet.error.exception");
            if (exception instanceof Exception) {
                logger.error("Exception during request processing", (Exception) exception);
            }
        }
    }

    /**
     * Logs details of the incoming HTTP request.
     */
    private void logRequestDetails(HttpServletRequest request) {
        logger.info("""
                        
                        Incoming Request:
                            Method={},
                            URI={},
                            QueryString={},
                            Headers={}
                        """,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(), requestHeaders(request));
    }

    /**
     * Logs details of the outgoing HTTP response.
     */
    private void logResponseDetails(HttpServletResponse response, String requestURI, Duration duration) {
        logger.info("""
                        
                        Outgoing Response:
                            Status={},
                            URI={},
                            Duration={}ms,
                            Headers={}
                        """,
                response.getStatus(),
                requestURI,
                duration.toMillis(), responseHeader(response));
    }

    private HashMap<String, Object> requestHeaders(HttpServletRequest request) {
        HashMap<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers;
    }

    private HashMap<String, Object> responseHeader(HttpServletResponse response) {
        HashMap<String, Object> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String name : headerNames) {
            headers.put(name, response.getHeader(name));
        }
        return headers;
    }
}
