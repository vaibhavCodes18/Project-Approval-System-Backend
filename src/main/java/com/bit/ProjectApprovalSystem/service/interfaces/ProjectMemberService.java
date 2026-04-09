package com.bit.ProjectApprovalSystem.service.interfaces;

import com.bit.ProjectApprovalSystem.dto.request.AddMemberRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectMembersListResponse;

public interface ProjectMemberService {

    ProjectMembersListResponse getProjectMembers(String id);

    void addProjectMember(String id, AddMemberRequest request);

    void removeProjectMember(String id, String studentId);
}
