package com.bit.ProjectApprovalSystem.entity;

import com.bit.ProjectApprovalSystem.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    private ObjectId id;
    
    private String title;
    
    private String description;
    
    private ObjectId leaderId;
    
    private ObjectId guideId;
    
    private ProjectStatus status;
    
    private int teamSize; // 1-4
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
