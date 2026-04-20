package com.devweaver.mapper;

import com.devweaver.dto.auth.RegisterRequest;
import com.devweaver.dto.auth.UserResponse;
import com.devweaver.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterRequest registerRequest);

    UserResponse toUserProfileResponse(User user);

}