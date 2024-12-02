package com.infinity.julien.user;

import com.infinity.julien.exception.exceptions.NotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public boolean existsByUsername(@NotBlank @Size(min = 3, max = 25) String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByUsername(email);
    }

    public User getUserById(String id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
