package com.example.stomp.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name="chat_room")
@SequenceGenerator(name = "chat_room_seq_generator",
        sequenceName= "CHAT_ROOM_NO_SEQ",
        initialValue = 1,
        allocationSize = 1
)

@DynamicInsert
@DynamicUpdate
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_room_seq_generator")
    @Column(name ="chat_room_id")
    private Long roomId;

    @Column
    private String name;

    @Builder
    public ChatRoom(Long roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

}