package com.bit.ProjectApprovalSystem.service.impl;

import com.bit.ProjectApprovalSystem.dto.response.DashboardResponse;
import com.bit.ProjectApprovalSystem.entity.Project;
import com.bit.ProjectApprovalSystem.entity.ProjectMember;
import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.ProjectStatus;
import com.bit.ProjectApprovalSystem.exception.ResourceNotFoundException;
import com.bit.ProjectApprovalSystem.repository.ProjectMemberRepository;
import com.bit.ProjectApprovalSystem.repository.ProjectRepository;
import com.bit.ProjectApprovalSystem.repository.UserRepository;
import com.bit.ProjectApprovalSystem.service.interfaces.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

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
    public DashboardResponse getStudentDashboard() {
        User student = getAuthenticatedUser();
        List<ProjectMember> memberships = projectMemberRepository.findByStudentId(student.getId());

        long totalProjects = memberships.size();
        long approvedProjects = 0;
        long pendingProjects = 0;
        long rejectedProjects = 0;

        for (ProjectMember member : memberships) {
            Project project = projectRepository.findById(member.getProjectId()).orElse(null);
            if (project != null) {
                if (project.getStatus() == ProjectStatus.HOD_APPROVED) {
                    approvedProjects++;
                } else if (project.getStatus() == ProjectStatus.SUBMITTED || project.getStatus() == ProjectStatus.GUIDE_APPROVED) {
                    pendingProjects++;
                } else if (project.getStatus() == ProjectStatus.GUIDE_REJECTED || project.getStatus() == ProjectStatus.HOD_REJECTED) {
                    rejectedProjects++;
                }
            }
        }

        return new DashboardResponse(totalProjects, approvedProjects, pendingProjects, rejectedProjects);
    }

    @Override
    public DashboardResponse getGuideDashboard() {
        User guide = getAuthenticatedUser();
        List<Project> projects = projectRepository.findByGuideId(guide.getId());

        long totalProjects = projects.size();
        long approvedProjects = 0;
        long pendingProjects = 0;
        long rejectedProjects = 0;

        for (Project project : projects) {
            if (project.getStatus() == ProjectStatus.GUIDE_APPROVED || project.getStatus() == ProjectStatus.HOD_APPROVED || project.getStatus() == ProjectStatus.HOD_REJECTED) {
                approvedProjects++;
            } else if (project.getStatus() == ProjectStatus.SUBMITTED) {
                pendingProjects++;
            } else if (project.getStatus() == ProjectStatus.GUIDE_REJECTED) {
                rejectedProjects++;
            }
        }

        return new DashboardResponse(totalProjects, approvedProjects, pendingProjects, rejectedProjects);
    }

    @Override
    public DashboardResponse getHodDashboard() {
        List<Project> projects = projectRepository.findAll();

        long totalProjects = projects.size();
        long approvedProjects = 0;
        long pendingProjects = 0;
        long rejectedProjects = 0;

        for (Project project : projects) {
            if (project.getStatus() == ProjectStatus.HOD_APPROVED) {
                approvedProjects++;
            } else if (project.getStatus() == ProjectStatus.GUIDE_APPROVED) {
                pendingProjects++;
            } else if (project.getStatus() == ProjectStatus.HOD_REJECTED) {
                rejectedProjects++;
            }
        }

        return new DashboardResponse(totalProjects, approvedProjects, pendingProjects, rejectedProjects);
    }
}
