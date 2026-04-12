package com.devweaver.service.impl;

import com.devweaver.dto.project.ProjectRequest;
import com.devweaver.dto.project.ProjectResponse;
import com.devweaver.dto.project.ProjectSummary;
import com.devweaver.entity.Project;
import com.devweaver.entity.User;
import com.devweaver.mapper.ProjectMapper;
import com.devweaver.repository.ProjectRepository;
import com.devweaver.repository.UserRepository;
import com.devweaver.exception.ResourceNotFoundException;
import com.devweaver.exception.UnauthorizedAccessException;
import com.devweaver.exception.UserNotFoundException;
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
    @Override
    public List<ProjectSummary> getUserProjects(Long userId) {
        return projectRepository.findAllAccessibleByUser(userId).stream()
                .map(projectMapper::toProjectSummary).collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {
       Project project = projectRepository.findAccessibleProjectById(id, userId)
               .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
       return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User creator = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("id", userId));
        Project project = Project
                .builder()
                .name(request.name())
                .creator(creator)
                .isPublic(false)
                .build();
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        if (!project.getCreator().getId().equals(userId)) {
            throw new UnauthorizedAccessException("update", "project");
        }
        project.setName(request.name());
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        if (!project.getCreator().getId().equals(userId)) {
            throw new UnauthorizedAccessException("delete", "project");
        }
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }
}
