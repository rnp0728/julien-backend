package com.infinity.julien.project;

import com.infinity.julien.exception.exceptions.AlreadyExistsException;
import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.organisation.OrganisationService;
import com.infinity.julien.project.payload.ProjectCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final OrganisationService organisationService;

    public Project create(ProjectCreationRequest request) throws NotFoundException {
        var organisation = organisationService.findById(request.organisation());
        if (projectRepository.existsByNameAndOrganisation(request.name(), organisation)) {
            throw new AlreadyExistsException("Project with name" + request.name() + " already exists in this " +
                    "organisation");
        }
        var project = projectMapper.toProject(request);
        project.setOrganisation(organisation);
        return projectRepository.save(project);
    }

    public Project findById(String id) throws NotFoundException {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project with ID" + id + " not " +
                "found"));
    }
}
