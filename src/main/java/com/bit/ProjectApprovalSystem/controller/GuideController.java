package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.request.ApprovalRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guide/projects")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @GetMapping
    public ResponseEntity<?> getProjectsAssignedToGuide() {
        List<ProjectResponse> responses = guideService.getProjectsAssignedToGuide();
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Assigned projects successfully fetched!", responses);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectDetailsForGuide(@PathVariable("id") String id) {
        ProjectResponse response = guideService.getProjectDetailsForGuide(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project details successfully fetched!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveProject(@PathVariable("id") String id, @RequestBody(required = false) ApprovalRequest request) {
        if (request == null) {
            request = new ApprovalRequest();
        }
        ProjectResponse response = guideService.approveProject(id, request);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project successfully approved by Guide!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectProject(@PathVariable("id") String id, @RequestBody(required = false) ApprovalRequest request) {
        if (request == null) {
            request = new ApprovalRequest();
        }
        ProjectResponse response = guideService.rejectProject(id, request);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project successfully rejected by Guide!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
