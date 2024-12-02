package com.infinity.julien.environment;

import com.infinity.julien.environment.payload.EnvironmentCreationRequest;
import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.payloads.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/environment")
@RequiredArgsConstructor
public class EnvironmentController {
    private final EnvironmentService environmentService;

    @PostMapping
    public ResponseEntity<?> createEnvironment(@RequestBody EnvironmentCreationRequest request) throws NotFoundException {
        return ResponseEntity.ok(ApiResponse.success("Successfully created environment", environmentService.create(request)));
    }

    @GetMapping("/{environmentId}")
    public ResponseEntity<?> getEnvironment(@PathVariable String environmentId) throws NotFoundException {
        return ResponseEntity.ok(ApiResponse.success("Environment Found!", environmentService.findById(environmentId)));
    }
}
