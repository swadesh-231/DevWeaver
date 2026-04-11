package com.devweaver.entity;

import com.devweaver.entity.enums.ChatEvent;
import com.devweaver.entity.enums.MessageRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    })
    private ChatSession chatSession;
    @Column(columnDefinition = "text")
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageRole role;
    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sequenceOrder ASC")
    private List<ChatEvents> events;
    private Integer tokenUsed = 0;

    @CreationTimestamp
    private Instant createdAt;

}
