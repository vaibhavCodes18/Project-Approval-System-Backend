package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.request.ProjectCreateRequest;
import com.bit.ProjectApprovalSystem.dto.request.ProjectUpdateRequest;
import com.bit.ProjectApprovalSystem.dto.request.ProjectFilterRequest;
import com.bit.ProjectApprovalSystem.dto.response.ProjectResponse;
import com.bit.ProjectApprovalSystem.dto.response.ApprovalHistoryResponse;
import com.bit.ProjectApprovalSystem.dto.response.ApprovalResponse;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.entity.Approval;
import com.bit.ProjectApprovalSystem.entity.Project;
import com.bit.ProjectApprovalSystem.entity.ProjectMember;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.ProjectMemberRole;
import com.bit.ProjectApprovalSystem.enums.ProjectStatus;
import com.bit.ProjectApprovalSystem.exception.BadCredentialsException;
import com.bit.ProjectApprovalSystem.exception.ResourceNotFoundException;
import com.bit.ProjectApprovalSystem.repository.ApprovalRepository;
import com.bit.ProjectApprovalSystem.repository.ProjectMemberRepository;
import com.bit.ProjectApprovalSystem.repository.ProjectRepository;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.service.interfaces.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        System.out.println(userEmail);
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request) {
        User leader = getAuthenticatedUser();

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setLeaderId(leader.getId());
        project.setStatus(ProjectStatus.DRAFT);
        project.setTeamSize(1);

        if (request.getGuideId() != null && !request.getGuideId().isEmpty()) {
            project.setGuideId(new ObjectId(request.getGuideId()));
        }

        Project savedProject = projectRepository.save(project);

        ProjectMember member = new ProjectMember();
        member.setProjectId(savedProject.getId());
        member.setStudentId(leader.getId());
        member.setRole(ProjectMemberRole.LEADER);
        member.setJoinedAt(LocalDateTime.now());
        projectMemberRepository.save(member);

        return mapToResponse(savedProject, leader);
    }

    @Override
    public com.bit.ProjectApprovalSystem.dto.response.ProjectListResponse getProjects(ProjectFilterRequest request) {
        List<Project> projects;

        ProjectStatus status = (request != null && request.getStatus() != null && !request.getStatus().isEmpty()) 
                ? ProjectStatus.valueOf(request.getStatus().toUpperCase()) : null;
        ObjectId guideId = (request != null && request.getGuideId() != null && !request.getGuideId().isEmpty()) 
                ? new ObjectId(request.getGuideId()) : null;

        List<ObjectId> projectIds = null;
        if (request != null && request.getStudentId() != null && !request.getStudentId().isEmpty()) {
            List<ProjectMember> memberships = projectMemberRepository.findByStudentId(new ObjectId(request.getStudentId()));
            if (memberships.isEmpty()) {
                return new com.bit.ProjectApprovalSystem.dto.response.ProjectListResponse(new ArrayList<>());
            }
            projectIds = memberships.stream().map(ProjectMember::getProjectId).collect(java.util.stream.Collectors.toList());
        }

        if (status != null && guideId != null && projectIds != null) {
            projects = projectRepository.findByStatusAndGuideIdAndIdIn(status, guideId, projectIds);
        } else if (status != null && guideId != null) {
            projects = projectRepository.findByStatusAndGuideId(status, guideId);
        } else if (status != null && projectIds != null) {
            projects = projectRepository.findByStatusAndIdIn(status, projectIds);
        } else if (guideId != null && projectIds != null) {
            projects = projectRepository.findByGuideIdAndIdIn(guideId, projectIds);
        } else if (status != null) {
            projects = projectRepository.findByStatus(status);
        } else if (guideId != null) {
            projects = projectRepository.findByGuideId(guideId);
        } else if (projectIds != null) {
            projects = projectRepository.findByIdIn(projectIds);
        } else {
            projects = projectRepository.findAll();
        }

        List<ProjectResponse> responses = projects.stream()
                .map(project -> mapToResponse(project, null))
                .collect(java.util.stream.Collectors.toList());
                
        return new com.bit.ProjectApprovalSystem.dto.response.ProjectListResponse(responses);
    }

    @Override
    public List<ProjectResponse> getMyProjects() {
        User user = getAuthenticatedUser();
        List<ProjectMember> memberships = projectMemberRepository.findByStudentId(user.getId());

        List<ProjectResponse> responses = new ArrayList<>();
        for (ProjectMember member : memberships) {
            Project project = projectRepository.findById(member.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            responses.add(mapToResponse(project, null));
        }
        return responses;
    }

    @Override
    public ProjectResponse getProjectById(String id) {
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return mapToResponse(project, null);
    }

    @Override
    public ProjectResponse updateProject(String id, ProjectUpdateRequest request) {
        User user = getAuthenticatedUser();
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getLeaderId().equals(user.getId())) {
            throw new BadCredentialsException("Only the project leader can update the project");
        }

        if (project.getStatus() != ProjectStatus.DRAFT) {
            throw new BadCredentialsException("Project can only be updated before submission");
        }

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        if (request.getGuideId() != null && !request.getGuideId().isEmpty()) {
            project.setGuideId(new ObjectId(request.getGuideId()));
        }

        Project updatedProject = projectRepository.save(project);
        return mapToResponse(updatedProject, user);
    }

    @Override
    public ProjectResponse submitProject(String id) {
        User user = getAuthenticatedUser();
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getLeaderId().equals(user.getId())) {
            throw new BadCredentialsException("Only the project leader can submit the project");
        }

        if (project.getStatus() != ProjectStatus.DRAFT) {
            throw new BadCredentialsException("Project must be in DRAFT status to be submitted");
        }

        if (project.getGuideId() == null) {
            throw new BadCredentialsException("Project must have a guide assigned before submission");
        }

        project.setStatus(ProjectStatus.SUBMITTED);
        Project updatedProject = projectRepository.save(project);
        return mapToResponse(updatedProject, user);
    }

    @Override
    @Transactional
    public void deleteProject(String id) {
        User user = getAuthenticatedUser();
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getLeaderId().equals(user.getId())) {
            throw new BadCredentialsException("Only the project leader can delete the project");
        }

        projectRepository.delete(project);

        // Also clean up project members
        List<ProjectMember> members = projectMemberRepository.findByProjectId(project.getId());
        projectMemberRepository.deleteAll(members);
    }

    @Override
    public ApprovalHistoryResponse getProjectApprovals(String id) {
        Project project = projectRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        List<Approval> approvals = approvalRepository.findByProjectIdOrderByActionAtAsc(project.getId());

        List<ApprovalResponse> approvalResponses = new ArrayList<>();
        for (Approval approval : approvals) {
            ApprovalResponse resp = ApprovalResponse.builder()
                    .id(approval.getId().toString())
                    .projectId(approval.getProjectId().toString())
                    .actionBy(approval.getActionBy().toString())
                    .role(approval.getRole() != null ? approval.getRole().name() : null)
                    .status(approval.getStatus() != null ? approval.getStatus().name() : null)
                    .remarks(approval.getRemark())
                    .actionAt(approval.getActionAt())
                    .build();
            approvalResponses.add(resp);
        }

        return ApprovalHistoryResponse.builder()
                .projectId(id)
                .approvals(approvalResponses)
                .build();
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
