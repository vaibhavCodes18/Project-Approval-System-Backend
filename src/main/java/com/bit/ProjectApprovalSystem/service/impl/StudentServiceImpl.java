package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.entity.ProjectMember;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.UserRole;
import com.bit.ProjectApprovalSystem.repository.ProjectMemberRepository;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.service.interfaces.StudentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Override
    public List<UserResponse> getUnassignedStudents() {
        // Find all student users
        List<User> allStudents = userRepository.findByRole(UserRole.STUDENT);

        // Get all student IDs that are currently assigned to any project
        List<ProjectMember> allMemberships = projectMemberRepository.findAll();
        Set<ObjectId> assignedStudentIds = allMemberships.stream()
                .map(ProjectMember::getStudentId)
                .collect(Collectors.toSet());

        // Filter and map to response
        return allStudents.stream()
                .filter(student -> !assignedStudentIds.contains(student.getId()))
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .enrollmentNo(user.getEnrollmentNo())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .status(user.getUserStatus() != null ? user.getUserStatus().name() : null)
                .build();
    }
}
