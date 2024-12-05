package com.infinity.julien.environment;

import com.infinity.julien.environment.payload.EnvironmentCreationRequest;
import com.infinity.julien.exception.exceptions.AlreadyExistsException;
import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.project.ProjectService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvironmentService {
    private final EnvironmentRepository environmentRepository;
    private final EnvironmentMapper environmentMapper;
    private final ProjectService projectService;

    public Environment create(EnvironmentCreationRequest request) throws NotFoundException, AlreadyExistsException {
        var project = projectService.findById(request.project());
        if (environmentRepository.existsByNameAndProject(request.name(), project)) {
            throw new AlreadyExistsException("Environment with name " + request.name() + " already exists");
        }
        var environment = environmentMapper.toEnvironment(request);
        environment.setProject(project);
        return environmentRepository.save(environment);
    }

    public Environment findById(@NonNull String id) throws NotFoundException {
        return environmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Environment with id " + id + " not found"));
    }
}
