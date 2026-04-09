package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.request.AddMemberRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectMembersListResponse;
import com.bit.ProjectApprovalSystem.dto.response.ProjectMemberResponse;
import com.bit.ProjectApprovalSystem.entity.Project;
import com.bit.ProjectApprovalSystem.entity.ProjectMember;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.ProjectMemberRole;
import com.bit.ProjectApprovalSystem.enums.ProjectStatus;
import com.bit.ProjectApprovalSystem.exception.BadCredentialsException;
import com.bit.ProjectApprovalSystem.exception.ResourceNotFoundException;
import com.bit.ProjectApprovalSystem.repository.ProjectMemberRepository;
import com.bit.ProjectApprovalSystem.repository.ProjectRepository;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.service.interfaces.ProjectMemberService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public ProjectMembersListResponse getProjectMembers(String id) {
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        List<ProjectMember> members = projectMemberRepository.findByProjectId(project.getId());
        List<ProjectMemberResponse> memberResponses = new ArrayList<>();
        
        for (ProjectMember member : members) {
            User student = userRepository.findById(member.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            memberResponses.add(ProjectMemberResponse.builder()
                    .studentId(student.getId().toString())
                    .name(student.getName())
                    .role(member.getRole().name())
                    .build());
        }
        
        return ProjectMembersListResponse.builder()
                .projectId(project.getId().toString())
                .members(memberResponses)
                .build();
    }

    @Override
    @Transactional
    public void addProjectMember(String id, AddMemberRequest request) {
        User user = getAuthenticatedUser();
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getLeaderId().equals(user.getId())) {
            throw new BadCredentialsException("Only the project leader can add members");
        }

        if (project.getStatus() != ProjectStatus.DRAFT) {
            throw new BadCredentialsException("Members can only be added before submission");
        }

        User studentToAdd = userRepository.findById(new ObjectId(request.getStudentId()))
                .orElseThrow(() -> new ResourceNotFoundException("Student to add not found"));
        
        if (projectMemberRepository.existsByProjectIdAndStudentId(project.getId(), studentToAdd.getId())) {
            throw new BadCredentialsException("Student is already a member of this project");
        }
        
        ProjectMember member = new ProjectMember();
        member.setProjectId(project.getId());
        member.setStudentId(studentToAdd.getId());
        member.setRole(ProjectMemberRole.MEMBER);
        member.setJoinedAt(LocalDateTime.now());
        projectMemberRepository.save(member);
        
        project.setTeamSize(project.getTeamSize() + 1);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void removeProjectMember(String id, String studentId) {
        User user = getAuthenticatedUser();
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getLeaderId().equals(user.getId())) {
            throw new BadCredentialsException("Only the project leader can remove members");
        }

        if (project.getStatus() != ProjectStatus.DRAFT) {
            throw new BadCredentialsException("Members can only be removed before submission");
        }

        ObjectId studentObjectId = new ObjectId(studentId);
        
        if (project.getLeaderId().equals(studentObjectId)) {
            throw new BadCredentialsException("Leader cannot be removed from the project");
        }

        ProjectMember member = projectMemberRepository.findByProjectIdAndStudentId(project.getId(), studentObjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project member not found"));
        
        projectMemberRepository.delete(member);
        
        project.setTeamSize(project.getTeamSize() - 1);
        projectRepository.save(project);
    }
}
