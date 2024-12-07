package com.infinity.julien.dbOps;

import com.infinity.julien.dbOps.dtos.MoveRequest;
import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.payloads.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/move")
    public ResponseEntity<?> moveCollections(@Valid @RequestBody MoveRequest request) throws Exception {
        var success = mongoDBOpsService.move(request);
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched db collections.", success));
    }
}
