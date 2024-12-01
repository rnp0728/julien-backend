package com.infinity.julien.organisation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends MongoRepository<Organisation, String> {
    Boolean existsByName(String name);
}
