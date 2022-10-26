package com.example.stomp.service;

import com.example.stomp.domain.entity.ChatRoom;
import com.example.stomp.dto.ChatMessageDto;
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
     * @param chatRoom 번호
     * @param path 경로
     */
    void chatRoomToFile(ChatRoom chatRoom, String path);

    /**
     * 채팅메시지 저장
     * @param id 번호
     * @param list 메시지 내역
     * @param path 경로
     */
    void saveChat(Long id, List<ChatMessageDto> list, String path);

    /**
     * 해당 채팅방의 채팅파일 반환
     * @param id 번호
     * @param path 경로
     * @return
     */
    List<String> getChats(Long id, String path);

    /**
     * 파일로부터 채팅메시지 문자열로 반환
     * @param dto 메시지
     * @return
     */
    String dtoToJson(ChatMessageDto dto);
}
