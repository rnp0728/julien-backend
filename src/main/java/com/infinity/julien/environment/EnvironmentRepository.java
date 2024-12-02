package com.infinity.julien.environment;

import com.infinity.julien.project.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentRepository extends MongoRepository<Environment, String> {
    public Boolean existsByNameAndProject(String name, Project project);
}
