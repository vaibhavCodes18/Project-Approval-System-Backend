package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.request.ApprovalRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectResponse;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.entity.Approval;
import com.bit.ProjectApprovalSystem.entity.Project;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.ApprovalRole;
import com.bit.ProjectApprovalSystem.enums.ApprovalStatus;
import com.bit.ProjectApprovalSystem.enums.ProjectStatus;
import com.bit.ProjectApprovalSystem.exception.BadCredentialsException;
import com.bit.ProjectApprovalSystem.exception.ResourceNotFoundException;
import com.bit.ProjectApprovalSystem.repository.ApprovalRepository;
import com.bit.ProjectApprovalSystem.repository.ProjectRepository;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.service.interfaces.GuideService;
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
public class GuideServiceImpl implements GuideService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<ProjectResponse> getProjectsAssignedToGuide() {
        User guide = getAuthenticatedUser();
        List<Project> projects = projectRepository.findByGuideId(guide.getId());
        List<ProjectResponse> responses = new ArrayList<>();
        for (Project project : projects) {
            responses.add(mapToResponse(project, null));
        }
        return responses;
    }

    @Override
    public ProjectResponse getProjectDetailsForGuide(String id) {
        User guide = getAuthenticatedUser();
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getGuideId().equals(guide.getId())) {
            throw new BadCredentialsException("You are not the guide for this project.");
        }

        return mapToResponse(project, null);
    }

    @Override
    @Transactional
    public ProjectResponse approveProject(String id, ApprovalRequest request) {
        User guide = getAuthenticatedUser();
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getGuideId().equals(guide.getId())) {
            throw new BadCredentialsException("You are not the guide for this project.");
        }

        if (project.getStatus() != ProjectStatus.SUBMITTED) {
            throw new BadCredentialsException("Project is not in SUBMITTED status.");
        }

        project.setStatus(ProjectStatus.GUIDE_APPROVED);
        Project updatedProject = projectRepository.save(project);

        Approval approval = new Approval();
        approval.setProjectId(project.getId());
        approval.setActionBy(guide.getId());
        approval.setRole(ApprovalRole.GUIDE);
        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setRemark(request.getRemarks());
        approval.setActionAt(LocalDateTime.now());
        approvalRepository.save(approval);

        return mapToResponse(updatedProject, null);
    }

    @Override
    @Transactional
    public ProjectResponse rejectProject(String id, ApprovalRequest request) {
        User guide = getAuthenticatedUser();
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getGuideId().equals(guide.getId())) {
            throw new BadCredentialsException("You are not the guide for this project.");
        }

        if (project.getStatus() != ProjectStatus.SUBMITTED) {
            throw new BadCredentialsException("Project is not in SUBMITTED status.");
        }

        project.setStatus(ProjectStatus.GUIDE_REJECTED);
        Project updatedProject = projectRepository.save(project);

        Approval approval = new Approval();
        approval.setProjectId(project.getId());
        approval.setActionBy(guide.getId());
        approval.setRole(ApprovalRole.GUIDE);
        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setRemark(request.getRemarks());
        approval.setActionAt(LocalDateTime.now());
        approvalRepository.save(approval);

        return mapToResponse(updatedProject, null);
    }

    @Override
    public List<com.bit.ProjectApprovalSystem.dto.response.GuideWithProjectsResponse> getAllGuidesWithProjects() {
        List<User> guides = userRepository.findByRole(com.bit.ProjectApprovalSystem.enums.UserRole.GUIDE);
        List<com.bit.ProjectApprovalSystem.dto.response.GuideWithProjectsResponse> overallResponses = new ArrayList<>();

        for (User guide : guides) {
            List<Project> assignedProjects = projectRepository.findByGuideId(guide.getId());
            List<ProjectResponse> projectResponses = new ArrayList<>();
            for (Project project : assignedProjects) {
                projectResponses.add(mapToResponse(project, null));
            }
            
            com.bit.ProjectApprovalSystem.dto.response.GuideWithProjectsResponse response = 
                com.bit.ProjectApprovalSystem.dto.response.GuideWithProjectsResponse.builder()
                    .id(guide.getId().toString())
                    .name(guide.getName())
                    .email(guide.getEmail())
                    .department(guide.getDepartment())
                    .assignedProjects(projectResponses)
                    .build();
                    
            overallResponses.add(response);
        }
        return overallResponses;
    }

    private ProjectResponse mapToResponse(Project project, User leader) {
        if (leader == null) {
            leader = userRepository.findById(project.getLeaderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Leader not found"));
        }

        UserResponse leaderResponse = null;
        if (leader != null) {
            leaderResponse = UserResponse.builder()
                    .id(leader.getId().toString())
                    .name(leader.getName())
                    .email(leader.getEmail())
                    .role(leader.getRole().name())
                    .status(leader.getUserStatus() != null ? leader.getUserStatus().name() : null)
                    .build();
        }

        UserResponse guideResponse = null;
        if (project.getGuideId() != null) {
            User guide = userRepository.findById(project.getGuideId()).orElse(null);
            if (guide != null) {
                guideResponse = UserResponse.builder()
                        .id(guide.getId().toString())
                        .name(guide.getName())
                        .email(guide.getEmail())
                        .role(guide.getRole().name())
                        .status(guide.getUserStatus() != null ? guide.getUserStatus().name() : null)
                        .build();
            }
        }

        return ProjectResponse.builder()
                .id(project.getId().toString())
                .title(project.getTitle())
                .description(project.getDescription())
                .leader(leaderResponse)
                .guide(guideResponse)
                .status(project.getStatus() != null ? project.getStatus().name() : null)
                .teamSize(project.getTeamSize())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
