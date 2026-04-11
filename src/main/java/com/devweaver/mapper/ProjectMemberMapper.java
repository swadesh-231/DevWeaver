package com.devweaver.mapper;

import com.devweaver.dto.member.MemberResponse;
import com.devweaver.entity.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.username")
    @Mapping(target = "imageUrl", source = "user.image_url")
    @Mapping(target = "role", source = "projectRole")
    @Mapping(target = "invitedAt", source = "invited_at")
    MemberResponse toMemberResponse(ProjectMember member);
}
