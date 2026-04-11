package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.request.CreateUserRequest;
import com.bit.ProjectApprovalSystem.dto.request.UserStatusRequest;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> saveGuide(@RequestBody CreateUserRequest createUserRequest) {
        UserResponse response = userService.saveGuide(createUserRequest);
        ApiResponse<?> apiResponse = new ApiResponse<>(201, "A guide was successfully created!", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserResponse> responses = userService.getAllUsers();
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Users successfully fetched!", responses);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        UserResponse response = userService.getUserById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "User successfully fetched!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable String id, @RequestBody UserStatusRequest request) {
        UserResponse response = userService.updateUserStatus(id, request.getStatus());
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "User status successfully updated!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
