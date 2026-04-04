package com.bit.ProjectApprovalSystem.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMembersListResponse {
    private String projectId;

    private List<ProjectMemberResponse> members;
}
