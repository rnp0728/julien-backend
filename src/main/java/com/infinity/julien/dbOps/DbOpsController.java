package com.infinity.julien.dbOps;

import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.payloads.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dbOps")
@RequiredArgsConstructor
public class DbOpsController {
    private final MongoDBOpsService mongoDBOpsService;

    @GetMapping("/collections/{environmentId}")
    public ResponseEntity<?> getCollections(
            @PathVariable @NotNull String environmentId
    ) throws NotFoundException {
        var collections = mongoDBOpsService.collections(environmentId);
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched db collections.", collections));
    }
}
