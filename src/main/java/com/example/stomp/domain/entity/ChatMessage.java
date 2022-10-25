package com.example.stomp.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name="chat_message")
@SequenceGenerator(name = "chat_message_seq_generator",
        sequenceName= "CHAT_MESSAGE_NO_SEQ",
        initialValue = 1,
        allocationSize = 1
)

@DynamicInsert
@DynamicUpdate
public class ChatMessage {

    @ManyToOne(fetch = FetchType.EAGER)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_message_seq_generator")
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column
    private String writer;

    @Column
    private String message;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime sendDate;//발송시간
}