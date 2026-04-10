package com.devweaver.mapper;

import com.devweaver.dto.project.ProjectResponse;
import com.devweaver.dto.project.ProjectSummary;
import com.devweaver.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    @Mapping(target = "owner", source = "creator")
    ProjectResponse toProjectResponse(Project project);
    ProjectSummary toProjectSummary(Project project);
}
