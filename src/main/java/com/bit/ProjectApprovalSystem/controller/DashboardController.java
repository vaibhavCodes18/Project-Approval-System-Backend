package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.response.DashboardResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/student")
    public ResponseEntity<?> getStudentDashboard() {
        DashboardResponse response = dashboardService.getStudentDashboard();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(200, "Student dashboard successfully fetched!", response));
    }

    @GetMapping("/guide")
    public ResponseEntity<?> getGuideDashboard() {
        DashboardResponse response = dashboardService.getGuideDashboard();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(200, "Guide dashboard successfully fetched!", response));
    }

    @GetMapping("/hod")
    public ResponseEntity<?> getHodDashboard() {
        DashboardResponse response = dashboardService.getHodDashboard();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(200, "HOD dashboard successfully fetched!", response));
    }
}
