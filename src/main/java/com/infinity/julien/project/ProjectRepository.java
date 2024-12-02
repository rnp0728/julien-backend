package com.infinity.julien.project;

import com.infinity.julien.organisation.Organisation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    public Boolean existsByNameAndOrganisation(String name, Organisation organisation);
}
