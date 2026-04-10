package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.request.ApprovalRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.HodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hod/projects")
public class HodController {

    @Autowired
    private HodService hodService;

    @GetMapping
    public ResponseEntity<?> getProjectsApprovedByGuide() {
        List<ProjectResponse> responses = hodService.getProjectsApprovedByGuide();
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Projects approved by Guide successfully fetched!", responses);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectDetailsForHod(@PathVariable("id") String id) {
        ProjectResponse response = hodService.getProjectDetailsForHod(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project details successfully fetched!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveProject(@PathVariable("id") String id, @RequestBody(required = false) ApprovalRequest request) {
        if (request == null) {
            request = new ApprovalRequest();
        }
        ProjectResponse response = hodService.approveProject(id, request);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project successfully approved by HOD!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectProject(@PathVariable("id") String id, @RequestBody(required = false) ApprovalRequest request) {
        if (request == null) {
            request = new ApprovalRequest();
        }
        ProjectResponse response = hodService.rejectProject(id, request);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project successfully rejected by HOD!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
