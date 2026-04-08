package com.bit.ProjectApprovalSystem.service.interfaces;

import com.bit.ProjectApprovalSystem.dto.request.ProjectCreateRequest;
import com.bit.ProjectApprovalSystem.dto.request.ProjectUpdateRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(ProjectCreateRequest request);

    List<ProjectResponse> getMyProjects();

    ProjectResponse getProjectById(String id);

    ProjectResponse updateProject(String id, ProjectUpdateRequest request);

    void deleteProject(String id);
}
