package com.example.stomp.domain.repository;

import com.example.stomp.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "SELECT * FROM chat_room ORDER BY chat_room_id DESC"
            ,nativeQuery = true )
    List<ChatRoom> findAllDesc();

}