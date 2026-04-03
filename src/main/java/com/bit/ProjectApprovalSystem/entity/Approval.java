package com.bit.ProjectApprovalSystem.entity;

import com.bit.ProjectApprovalSystem.enums.ApprovalRole;
import com.bit.ProjectApprovalSystem.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {

    @Id
    private ObjectId id;
    
    private ObjectId projectId;
    
    private ObjectId actionBy; // The user ID performing the action
    
    private ApprovalRole role;
    
    private ApprovalStatus status;
    
    private String remark;
    
    @CreatedDate
    private LocalDateTime actionAt;

}
