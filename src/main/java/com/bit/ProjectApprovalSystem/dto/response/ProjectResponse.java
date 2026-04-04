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
public class ProjectResponse {
    private String id;

    private String title;

    private String description;

    private UserResponse leader;

    private UserResponse guide;

    private String status;

    private Integer teamSize;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
