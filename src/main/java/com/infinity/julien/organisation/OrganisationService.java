package com.infinity.julien.organisation;

import com.infinity.julien.exception.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationService {
    private final OrganisationRepository organisationRepository;

    public Organisation createOrganisation(Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    public boolean existsByName(String name) {
        return organisationRepository.existsByName(name);
    }

    public List<Organisation> findAll() {
        return organisationRepository.findAll();
    }

    public Organisation findById(String id) throws NotFoundException {
        return organisationRepository.findById(id).orElseThrow(() -> new NotFoundException("Organisation not found"));
    }
}
