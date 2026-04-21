package com.devweaver.service;

import com.devweaver.dto.project.ProjectRequest;
import com.devweaver.dto.project.ProjectResponse;
import com.devweaver.dto.project.ProjectSummary;

import java.util.List;

public interface ProjectService {
    List<ProjectSummary> getUserProjects();

    ProjectResponse getUserProjectById(Long projectId);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long projectId, ProjectRequest request);

    void softDelete(Long projectId);
}
