package com.infinity.julien.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infinity.julien.organisation.Organisation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projects")
public class Project {
    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 20, message = "Project name must be in between 3 - 20 characters")
    private String name;

    @NotBlank
    @Size(min = 5, max = 100, message = "Description should be in between 5 - 100 characters")
    private String description;

    @DBRef
    @NotNull(message = "Organisation can't be null.")
    private Organisation organisation;

    @NotNull(message = "Db is required. (MongoDB/PostgresQL)")
    private Db db;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updatedAt;

    @Version
    private Integer version;
}
