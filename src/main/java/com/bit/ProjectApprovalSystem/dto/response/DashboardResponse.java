package com.bit.ProjectApprovalSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Long totalProjects;

    private Long approvedProjects;

    private Long pendingProjects;

    private Long rejectedProjects;
}
