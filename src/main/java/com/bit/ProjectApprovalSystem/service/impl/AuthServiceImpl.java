package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.request.RegisterRequest;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.UserRole;
import com.bit.ProjectApprovalSystem.enums.UserStatus;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
