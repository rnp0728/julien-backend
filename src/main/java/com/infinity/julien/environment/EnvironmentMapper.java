package com.infinity.julien.environment;

import com.infinity.julien.environment.payload.EnvironmentCreationRequest;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentMapper {
    public Environment toEnvironment(EnvironmentCreationRequest request) {
        return Environment.builder()
                .name(request.name())
                .dbHost(request.dbHost())
                .dbUser(request.dbUser())
                .dbPassword(request.dbPassword())
                .build();
    }
}
