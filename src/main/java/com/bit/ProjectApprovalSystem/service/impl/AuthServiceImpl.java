package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.request.CreateUserRequest;
import com.bit.ProjectApprovalSystem.dto.request.LoginRequest;
import com.bit.ProjectApprovalSystem.dto.request.RegisterRequest;
import com.bit.ProjectApprovalSystem.dto.response.AuthResponse;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.UserRole;
import com.bit.ProjectApprovalSystem.enums.UserStatus;
import com.bit.ProjectApprovalSystem.exception.BadCredentialsException;
import com.bit.ProjectApprovalSystem.exception.DuplicateResourceException;
import com.bit.ProjectApprovalSystem.exception.ResourceNotFoundException;
import com.bit.ProjectApprovalSystem.repository.RefreshTokenRepository;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.security.JwtService;
import com.bit.ProjectApprovalSystem.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserResponse saveStudent(RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(UserRole.STUDENT);
        user.setUserStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();;

        response.setId(savedUser.getId().toString());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().name());
        response.setStatus(savedUser.getUserStatus().name());

        return response;
    }

    @Override
    public UserResponse saveHod(CreateUserRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException(
                    "A user with email '" + registerRequest.getEmail() + "' already exists");
        }
        if (userRepository.existsByRole(UserRole.valueOf(registerRequest.getRole())) && ("HOD".equals(registerRequest.getRole()))) {
            throw new DuplicateResourceException("An hod already exists");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(UserRole.valueOf(registerRequest.getRole()));
        user.setUserStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();;

        response.setId(savedUser.getId().toString());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().name());
        response.setStatus(savedUser.getUserStatus().name());

        return response;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Users with this email is invalid."));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if(!authentication.isAuthenticated()){
            throw new BadCredentialsException("Users credentials are invalid.");
        }

        String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        com.bit.ProjectApprovalSystem.entity.RefreshToken rt = new com.bit.ProjectApprovalSystem.entity.RefreshToken();
        rt.setToken(refreshToken);
        rt.setUserId(user.getId());
        rt.setRevoked(false);
        refreshTokenRepository.save(rt);
        
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId().toString());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole().name());
        userResponse.setStatus(user.getUserStatus().name());

        return AuthResponse.builder()
                .token(accessToken)
                .user(userResponse)
                .build();
    }

    @Override
    public void logout(com.bit.ProjectApprovalSystem.dto.request.LogoutRequest logoutRequest) {
        if (logoutRequest == null || logoutRequest.getRefreshToken() == null) {
            throw new BadCredentialsException("Refresh token is required for logout");
        }

        com.bit.ProjectApprovalSystem.entity.RefreshToken rfToken = refreshTokenRepository.findByToken(logoutRequest.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));
        
        rfToken.setRevoked(true);
        refreshTokenRepository.save(rfToken);
    }
}
