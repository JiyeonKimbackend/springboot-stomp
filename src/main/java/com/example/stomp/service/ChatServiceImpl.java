package com.example.stomp.service;

import com.example.stomp.domain.entity.ChatRoom;
import com.example.stomp.domain.repository.ChatRoomRepository;
import com.example.stomp.dto.ChatRoomRequestDto;
import com.example.stomp.dto.ChatRoomResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final ChatRoomRepository chatRoomRepo;

    @Override
    @Transactional(readOnly = true) //조회기능만 유지
    public List<ChatRoomResponseDto> findAllRooms(){
        return chatRoomRepo.findAllDesc().stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());//repo결과값->map을 통해 dto로변환->list변환
    }

    @Override
    public ChatRoomResponseDto findRoomById(Long id){
        ChatRoom entity = chatRoomRepo.findById(id).get();
        return new ChatRoomResponseDto(entity);
    }


    @Override
    public ChatRoom createChatRoom(String name){

        ChatRoomRequestDto dto = ChatRoomRequestDto.builder()
                .name(name)
                .build();

        ChatRoom room = dto.toEntity();
        chatRoomRepo.save(room);

        return room;
    }

    @Override
    public void chatRoomToFile(ChatRoom chatRoom, String path){
        StringBuilder sb = new StringBuilder(path);
        sb.append(File.separator).append(chatRoom.getName()).append(File.separator);//파일경로: path [채팅방이름]

        File chatPath = new File(sb.toString());

        if(!chatPath.exists())
            chatPath.mkdirs();

        String chatRoomName = chatRoom.getRoomId() + "_" + chatRoom.getName() + ".txt";//파일이름: [채팅방번호]_[채팅방이름].txt
        File chatRoomFile = new File(sb.toString(), chatRoomName);

        if(!chatRoomFile.exists()) {
            try {
                chatRoomFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
