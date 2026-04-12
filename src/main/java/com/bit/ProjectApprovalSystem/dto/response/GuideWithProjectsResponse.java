package com.bit.ProjectApprovalSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideWithProjectsResponse {
    private String id;
    private String name;
    private String email;
    private String department;
    private List<ProjectResponse> assignedProjects;
}
