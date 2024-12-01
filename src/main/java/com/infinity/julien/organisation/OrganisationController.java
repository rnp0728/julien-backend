package com.infinity.julien.organisation;

import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.payloads.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organisation")
@RequiredArgsConstructor
public class OrganisationController {
    private final OrganisationService organisationService;

    @GetMapping
    public ResponseEntity<?> getOrganisations() {
        return ResponseEntity.ok(organisationService.findAll());
    }

    @GetMapping("/{organisationId}")
    public ResponseEntity<?> getOrganisation(@PathVariable String organisationId) throws NotFoundException {
        var organisation = organisationService.findById(organisationId);
        return ResponseEntity.ok(ApiResponse.success("Successfully retrieved organisation", organisation));
    }
}
