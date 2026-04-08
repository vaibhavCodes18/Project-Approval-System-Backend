package com.bit.ProjectApprovalSystem.service.interfaces;

import com.bit.ProjectApprovalSystem.dto.request.CreateUserRequest;
import com.bit.ProjectApprovalSystem.dto.request.LoginRequest;
import com.bit.ProjectApprovalSystem.dto.request.RegisterRequest;
import com.bit.ProjectApprovalSystem.dto.response.AuthResponse;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;

public interface AuthService {
    UserResponse saveStudent(RegisterRequest registerRequest);

    UserResponse saveHod(CreateUserRequest createUserRequest);

    AuthResponse login(LoginRequest loginRequest);

    void logout(com.bit.ProjectApprovalSystem.dto.request.LogoutRequest logoutRequest);
}
