package com.example.stomp.dto;

import com.example.stomp.domain.entity.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private Long roomId;
    private String name;
//    private Set<WebSocketSession> sessions = new HashSet<>();
    //WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션

    public ChatRoomResponseDto(ChatRoom entity) {
        this.roomId = entity.getRoomId();
        this.name = entity.getName();
    }



}