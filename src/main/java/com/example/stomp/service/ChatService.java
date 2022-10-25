package com.example.stomp.service;

import com.example.stomp.domain.entity.ChatRoom;
import com.example.stomp.dto.ChatRoomResponseDto;

import java.util.List;

public interface ChatService {
    /**
     * 채팅방을 최신순으로 반환
     * @return
     */
    List<ChatRoomResponseDto> findAllRooms();

    /**
     * 특정 번호에 해당하는 채팅방 반환
     * @param id 번호
     * @return
     */
    ChatRoomResponseDto findRoomById(Long id);

    /**
     * 채팅방을 생성
     * @param name 이름
     * @return
     */
    ChatRoom createChatRoom(String name);

    /**
     * 해당 채팅방 파일 생성
     * @param chatRoom
     * @param path
     */
    void chatRoomToFile(ChatRoom chatRoom, String path);
}
