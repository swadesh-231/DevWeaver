package com.devweaver.entity;

import com.devweaver.entity.enums.MessageRole;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private Long id;
    private ChatSession chatSession;
    private String content;
    private MessageRole role;
    private String toolCalls;
    private Integer tokensUsed;
    private Instant createdAt;

}
