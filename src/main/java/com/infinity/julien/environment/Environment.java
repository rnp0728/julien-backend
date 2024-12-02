package com.infinity.julien.environment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infinity.julien.project.Project;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "environments")
public class Environment {
    @Id
    private String id;
    @NotBlank
    private String name;

    @Valid
    private Project project;
    @NotBlank
    private String dbUrl;
    @NotBlank
    private String dbUser;
    @NotBlank
    private String dbPassword;
    @NotBlank
    private String dbName;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updatedAt;

    @Version
    private Integer version;
}
