package com.example.stomp.domain.repository;

import com.example.stomp.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM chat_room c ORDER BY c.chat_room_id DESC")
    List<ChatRoom> findAllDesc();

}