package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.request.CreateUserRequest;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.UserRole;
import com.bit.ProjectApprovalSystem.enums.UserStatus;
import com.bit.ProjectApprovalSystem.exception.BadCredentialsException;
import com.bit.ProjectApprovalSystem.exception.DuplicateResourceException;
import com.bit.ProjectApprovalSystem.exception.ResourceNotFoundException;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.service.interfaces.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public UserResponse saveGuide(CreateUserRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException(
                    "A user with email '" + registerRequest.getEmail() + "' already exists");
        }

        if(!getAuthenticatedUser().getRole().equals(UserRole.HOD)){
            throw new BadCredentialsException(
                    "Only hod can create guide you don't have a right to create guide.");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(UserRole.GUIDE);
        user.setUserStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(savedUser.getId().toString());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().name());
        response.setStatus(savedUser.getUserStatus().name());

        return response;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse updateUserStatus(String id, String status) {
        User user = userRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        try {
            UserStatus newStatus = UserStatus.valueOf(status.toUpperCase());
            user.setUserStatus(newStatus);
            User updatedUser = userRepository.save(user);
            return mapToUserResponse(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid status provided. Allowed values are ACTIVE, INACTIVE.");
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .status(user.getUserStatus().name())
                .build();
    }
}
