package com.infinity.julien.auth;

import com.infinity.julien.auth.payloads.AuthenticatedResponse;
import com.infinity.julien.auth.payloads.RefreshRequest;
import com.infinity.julien.auth.payloads.SigninRequest;
import com.infinity.julien.auth.payloads.SignupRequest;
import com.infinity.julien.exception.exceptions.AlreadyExistsException;
import com.infinity.julien.exception.exceptions.InvalidTokenException;
import com.infinity.julien.exception.exceptions.NotFoundException;
import com.infinity.julien.organisation.Organisation;
import com.infinity.julien.organisation.OrganisationService;
import com.infinity.julien.role.ERole;
import com.infinity.julien.role.Role;
import com.infinity.julien.role.RoleRepository;
import com.infinity.julien.security.jwt.JwtUtils;
import com.infinity.julien.security.service.UserDetailsImpl;
import com.infinity.julien.token.TokenService;
import com.infinity.julien.user.User;
import com.infinity.julien.user.UserRepository;
import com.infinity.julien.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganisationService organisationService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtService;
    private final TokenService tokenService;
    private final UserService userService;

    public void createUser(SignupRequest signUpRequest) throws NotFoundException {
        // Create new user's account
        User user = User.builder()
                .username(signUpRequest.email())
                .password(encoder.encode(signUpRequest.password()))
                .build();

        // Role
        Set<String> strRoles = signUpRequest.roles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new NotFoundException("Role is not found."));
            roles.add(userRole);
        } else {
            for (String role : strRoles) {
                if (role.equalsIgnoreCase("admin")) {
                    Role adminRole = roleRepository.findByName(ERole.ADMIN)
                            .orElseThrow(() -> new NotFoundException("Role ADMIN is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.USER)
                            .orElseThrow(() -> new NotFoundException("Role USER is not found."));
                    roles.add(userRole);
                }
            }
        }
        // Organisation
        if (organisationService.existsByName(signUpRequest.organisation())) {
            throw new AlreadyExistsException("Organization already exists.");
        }

        var organisation = organisationService.createOrganisation(Organisation.builder()
                .name(signUpRequest.organisation())
                .build());
        user.setOrganisation(organisation);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public AuthenticatedResponse authenticateUser(SigninRequest signinRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.email(),
                        signinRequest.password()
                )
        );

        var user = userRepository.findByUsername(signinRequest.email())
                .orElseThrow();
        return getAuthenticatedResponse(user);
    }

    public AuthenticatedResponse refreshToken(RefreshRequest request) throws NotFoundException, InvalidTokenException {
        String refreshToken = request.refreshToken();
        if (!jwtService.validateJwtToken(refreshToken)) {
            throw new InvalidTokenException("Error: Refresh token is invalid.");
        }
        String email = jwtService.extractUsername(refreshToken);
        var user = userService.getUser(email).orElseThrow(() -> new NotFoundException("User not Found."));

        return getAuthenticatedResponse(user);
    }

    private AuthenticatedResponse getAuthenticatedResponse(User user) {
        var userDetails = UserDetailsImpl.build(user);
        var newAccessToken = jwtService.generateToken(userDetails);
        var newRefreshToken = jwtService.generateRefreshToken(userDetails);
        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, newAccessToken);

        return new AuthenticatedResponse(newAccessToken, newRefreshToken, user);
    }
}
