package com.bit.ProjectApprovalSystem.entity;

import com.bit.ProjectApprovalSystem.enums.UserRole;
import com.bit.ProjectApprovalSystem.enums.UserStatus;
import com.mongodb.lang.Nullable;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private ObjectId id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String department;
    @Nullable
    private String enrollmentNo;
    private UserRole role;
    private UserStatus userStatus;
    @CreatedDate
    private LocalDateTime createdAt;
}
