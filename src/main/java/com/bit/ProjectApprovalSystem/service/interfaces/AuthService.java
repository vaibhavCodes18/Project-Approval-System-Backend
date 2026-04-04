package com.bit.ProjectApprovalSystem.service.interfaces;

import com.bit.ProjectApprovalSystem.dto.request.RegisterRequest;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;

public interface AuthService {
    UserResponse saveStudent(RegisterRequest registerRequest);
}
