package com.devweaver.service;

import com.devweaver.dto.auth.UserResponse;

public interface UserService {
    UserResponse getProfile(Long userId);
}
