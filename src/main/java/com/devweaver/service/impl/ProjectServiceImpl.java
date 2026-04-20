package com.devweaver.service.impl;

import com.devweaver.dto.project.ProjectRequest;
import com.devweaver.dto.project.ProjectResponse;
import com.devweaver.dto.project.ProjectSummary;
import com.devweaver.entity.Project;
import com.devweaver.entity.ProjectMember;
import com.devweaver.entity.ProjectMemberId;
import com.devweaver.entity.User;
import com.devweaver.entity.enums.ProjectRoles;
import com.devweaver.mapper.ProjectMapper;
import com.devweaver.repository.ProjectMemberRepository;
import com.devweaver.repository.ProjectRepository;
import com.devweaver.repository.UserRepository;
import com.devweaver.exception.ResourceNotFoundException;
import com.devweaver.exception.UnauthorizedAccessException;
import com.devweaver.exception.UserNotFoundException;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.ProjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;
    private final JwtUtils jwtUtils;
    @Override
    public List<ProjectSummary> getUserProjects() {
        Long userId = jwtUtils.getCurrentUserId();
        return projectRepository.findAllAccessibleByUser(userId).stream()
                .map(projectMapper::toProjectSummary).collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getUserProjectById(Long id) {
        Long userId = jwtUtils.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        Long userId = jwtUtils.getCurrentUserId();
        User creator = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("id", userId));
        Project project = Project
                .builder()
                .name(request.name())
                .isPublic(false)
                .build();
        projectRepository.save(project);
        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), creator.getId());
        ProjectMember projectMember = ProjectMember.builder()
                .projectMemberId(projectMemberId)
                .projectRole(ProjectRoles.CREATOR)
                .user(creator)
                .accepted_at(Instant.now())
                .invited_at(Instant.now())
                .project(project)
                .build();
        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Long userId = jwtUtils.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        project.setName(request.name());
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id) {
        Long userId = jwtUtils.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        ProjectRoles role = projectMemberRepository.findRoleByProjectIdAndUserId(id, userId)
                .orElseThrow(() -> new UnauthorizedAccessException("delete", "project"));
        if (role != ProjectRoles.CREATOR) {
            throw new UnauthorizedAccessException("delete", "project");
        }
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }
}
