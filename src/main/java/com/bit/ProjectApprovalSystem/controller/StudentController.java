package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/unassigned")
    public ResponseEntity<?> getUnassignedStudents() {
        List<UserResponse> responses = studentService.getUnassignedStudents();
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Unassigned students successfully fetched!", responses);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
