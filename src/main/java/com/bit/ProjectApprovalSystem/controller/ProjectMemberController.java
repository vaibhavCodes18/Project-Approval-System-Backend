package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.request.AddMemberRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectMembersListResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{id}/members")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<?> getProjectMembers(@PathVariable("id") String id) {
        ProjectMembersListResponse response = projectMemberService.getProjectMembers(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Project members successfully fetched!", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping
    public ResponseEntity<?> addProjectMember(@PathVariable("id") String id, @RequestBody AddMemberRequest request) {
        projectMemberService.addProjectMember(id, request);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Member successfully added to project!", null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> removeProjectMember(@PathVariable("id") String id, @PathVariable("studentId") String studentId) {
        projectMemberService.removeProjectMember(id, studentId);
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Member successfully removed from project!", null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
