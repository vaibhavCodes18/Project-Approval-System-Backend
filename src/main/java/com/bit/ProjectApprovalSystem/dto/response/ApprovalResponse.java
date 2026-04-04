package com.bit.ProjectApprovalSystem.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalResponse {
    private String id;

    private String projectId;

    private String actionBy;

    private String role;   // GUIDE / HOD

    private String status; // APPROVED / REJECTED

    private String remarks;

    private LocalDateTime actionAt;
}
