package com.infinity.julien.project;

import com.infinity.julien.project.payload.ProjectCreationRequest;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {

    public Project toProject(ProjectCreationRequest request) {
        return Project.builder()
                .name(request.name())
                .description(request.description())
                .db(request.db())
                .build();
    }
}
