package com.bit.ProjectApprovalSystem.service.interfaces;

import com.bit.ProjectApprovalSystem.dto.request.CreateUserRequest;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse saveGuide(CreateUserRequest createUserRequest);

    UserResponse saveHod(CreateUserRequest createUserRequest);

    UserResponse getUserById(String id);

    UserResponse updateUserStatus(String id, String status);
}
