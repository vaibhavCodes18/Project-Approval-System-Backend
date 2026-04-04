package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.request.RegisterRequest;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> addStudent(@RequestBody RegisterRequest registerRequest){
        UserResponse response = authService.saveStudent(registerRequest);
        ApiResponse<?> apiResponse = new ApiResponse<>(201, "done", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
