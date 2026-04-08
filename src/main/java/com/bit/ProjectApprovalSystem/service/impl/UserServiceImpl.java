package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.UserStatus;
import com.bit.ProjectApprovalSystem.exception.BadCredentialsException;
import com.bit.ProjectApprovalSystem.exception.ResourceNotFoundException;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.service.interfaces.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
