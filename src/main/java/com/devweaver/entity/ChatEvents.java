package com.devweaver.entity;

import com.devweaver.entity.enums.ChatEvent;
import jakarta.persistence.*;
import lombok.*;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_events")
public class ChatEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ChatMessage chatMessage;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatEvent chatEvent;
    @Column(nullable = false)
    private Integer sequenceOrder;
    @Column(columnDefinition = "text")
    private String content;

    private String filePath;
    @Column(columnDefinition = "text")
    private String metaData;
}
