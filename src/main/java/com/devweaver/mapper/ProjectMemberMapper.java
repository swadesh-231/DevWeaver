package com.devweaver.mapper;

import com.devweaver.dto.member.MemberResponse;
import com.devweaver.entity.Project;
import com.devweaver.entity.ProjectMember;
import com.devweaver.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
    @Mapping(target = "userId",source = "id")
    @Mapping(target = "projectRoles", constant = "CREATOR")
    MemberResponse toMemberResponseFromCreator(User creator);

    @Mapping(target = "userId",source = "user.id")
    @Mapping(target = "email",source = "user.email")
    @Mapping(target = "name",source = "user.username")
    @Mapping(target = "projectRoles", source = "projectRole")
    MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);
}
