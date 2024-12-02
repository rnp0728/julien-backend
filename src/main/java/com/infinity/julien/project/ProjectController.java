package com.infinity.julien.project;

import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.payloads.ApiResponse;
import com.infinity.julien.project.payload.ProjectCreationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> createProject(
            @Valid @RequestBody ProjectCreationRequest request
    ) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", projectService.create(request)));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProject(
            @PathVariable String projectId
    ) throws NotFoundException {
        return ResponseEntity.ok(ApiResponse.success("Project found!", projectService.findById(projectId)));
    }
}
