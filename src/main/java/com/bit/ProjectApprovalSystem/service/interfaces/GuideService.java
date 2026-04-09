package com.bit.ProjectApprovalSystem.service.interfaces;

import com.bit.ProjectApprovalSystem.dto.request.ApprovalRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectResponse;

import java.util.List;

public interface GuideService {
    List<ProjectResponse> getProjectsAssignedToGuide();
    ProjectResponse getProjectDetailsForGuide(String id);
    ProjectResponse approveProject(String id, ApprovalRequest request);
    ProjectResponse rejectProject(String id, ApprovalRequest request);
}
