package com.devweaver.dto.chat;

import com.devweaver.entity.enums.ChatEvent;

public record ChatEventResponse(
        Long id,
        ChatEvent type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
