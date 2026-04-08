package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.request.ProjectCreateRequest;
import com.bit.ProjectApprovalSystem.dto.request.ProjectUpdateRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectCreateRequest request) {
        ProjectResponse response = projectService.createProject(request);
        ApiResponse<?> apiResponse = new ApiResponse<>(201, "Project successfully created!", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyProjects() {
        List<ProjectResponse> responses = projectService.getMyProjects();
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "My projects successfully fetched!", responses);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable("id") String id) {
        ProjectResponse response = projectService.getProjectById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project successfully fetched!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable("id") String id, @RequestBody ProjectUpdateRequest request) {
        ProjectResponse response = projectService.updateProject(id, request);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project successfully updated!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") String id) {
        projectService.deleteProject(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project successfully deleted!", null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
