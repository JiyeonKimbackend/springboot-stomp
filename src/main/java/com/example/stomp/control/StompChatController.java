package com.example.stomp.control;

import com.example.stomp.dto.BufferMap;
import com.example.stomp.dto.ChatMessageDto;
import com.example.stomp.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StompChatController {
    private final ChatService service;
    private final BufferMap<Long, List<ChatMessageDto>> chatListMap;
    private final HttpServletRequest request;
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    //채팅방 입장
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDto message){
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    //채팅 메시지 발송
    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message){
//        message.setTime(getDate());
        Long roomId = message.getRoomId();

        if(!chatListMap.containsKey(roomId)) {//해당 채팅방번호의 list가 없을 시 새로 생성
            chatListMap.put(roomId, new ArrayList<>());
        }

        List<ChatMessageDto> chatList = chatListMap.get(roomId);//각각의 채팅메시지 list로 저장
        chatList.add(message);
        service.saveChat(roomId, chatList, request.getServletContext().getRealPath("/chat"));
        chatList.clear();

        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    //채팅방 퇴장
    @MessageMapping(value = "/chat/exit")
    public void exit(ChatMessageDto message){
//        message.setTime(getDate());
        message.setMessage(message.getWriter() + "님이 퇴장하셨습니다.");//퇴장 메시지

        Long roomId = message.getRoomId();
        service.saveChat(roomId, chatListMap.get(roomId), request.getServletContext().getRealPath("/chat"));//메시지 저장
        chatListMap.remove(roomId);//버퍼 삭제

        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}