package com.infinity.julien.project.payload;

import com.infinity.julien.project.Db;
import jakarta.validation.constraints.NotBlank;

public record ProjectCreationRequest(
        @NotBlank
        String name,

        String description,

        String organisation,

        Db db
) {
}
