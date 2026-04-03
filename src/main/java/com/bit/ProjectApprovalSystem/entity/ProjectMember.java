package com.bit.ProjectApprovalSystem.entity;

import com.bit.ProjectApprovalSystem.enums.ProjectMemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "project_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

    @Id
    private ObjectId id;
    
    private ObjectId projectId;
    
    private ObjectId studentId;
    
    private ProjectMemberRole role;
    
    @CreatedDate
    private LocalDateTime joinedAt;

}
