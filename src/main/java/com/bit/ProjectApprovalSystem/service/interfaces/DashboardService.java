package com.bit.ProjectApprovalSystem.service.interfaces;

import com.bit.ProjectApprovalSystem.dto.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getStudentDashboard();
    DashboardResponse getGuideDashboard();
    DashboardResponse getHodDashboard();
}
