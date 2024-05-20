package com.thewinningteam.pms.message;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String recipient;

    @Lob
    private String content;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        SENT, DELIVERED, READ
    }
}

