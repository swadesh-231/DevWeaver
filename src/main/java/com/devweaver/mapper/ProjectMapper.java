package com.devweaver.mapper;

import com.devweaver.dto.project.ProjectResponse;
import com.devweaver.dto.project.ProjectSummary;
import com.devweaver.entity.Project;
import com.devweaver.entity.enums.ProjectRoles;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    ProjectResponse toProjectSummaryResponse(Project project, ProjectRoles role);

    List<ProjectResponse> toListOfProjectSummaryResponse(List<Project> projects);

    ProjectSummary toProjectSummary(Project project);

}
