package com.example.stomp.service;

import com.example.stomp.domain.entity.ChatRoom;
import com.example.stomp.domain.repository.ChatRoomRepository;
import com.example.stomp.dto.ChatMessageDto;
import com.example.stomp.dto.ChatRoomRequestDto;
import com.example.stomp.dto.ChatRoomResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
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

    @Override
    public void saveChat(Long id, List<ChatMessageDto> list, String path) {
        if(list.isEmpty()) return;

        List<String> jsonList = list.stream()
                                .map(c -> dtoToJson(c))
                                    .collect(Collectors.toList());//json형태로 변환

        ChatRoom room = chatRoomRepo.findById(id).get();

        //파일경로
        StringBuilder chatPath = new StringBuilder(path);
        chatPath.append(room.getRoomId()).append("_").append(room.getName()).append(".txt");

        try {//문자열 .txt에 저장
            BufferedWriter out = new BufferedWriter(new FileWriter(path.toString(), true));

            for(String chat : jsonList){
                out.write(chat);
                out.append('\n');
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getChats(Long id, String path) {
        ChatRoom room = chatRoomRepo.findById(id).get();
        StringBuilder chatPath = new StringBuilder(path);
        chatPath.append(room.getRoomId()).append("_").append(room.getName()).append(".txt");//파일 가져오기

        List<String> chats = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(chatPath.toString()))){//문자열 가져오기

            while(br.ready()) {
                String chat = br.readLine();
                chats.add(chat);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return chats;
    }

    @Override
    public String dtoToJson(ChatMessageDto dto) {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("dto = " + dto);
        String chatMessage = null;
        try {
            chatMessage = mapper.writeValueAsString(dto);
            System.out.println("chat = " + chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return chatMessage;
    }
}
