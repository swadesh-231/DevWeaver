package com.devweaver.service.impl;

import com.devweaver.dto.auth.UserResponse;
import com.devweaver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public UserResponse getProfile(Long userId) {
        return null;
    }
}
