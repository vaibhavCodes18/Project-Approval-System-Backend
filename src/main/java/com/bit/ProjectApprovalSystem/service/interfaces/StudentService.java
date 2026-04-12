package com.bit.ProjectApprovalSystem.service.interfaces;

import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import java.util.List;

public interface StudentService {
    List<UserResponse> getUnassignedStudents();
}
