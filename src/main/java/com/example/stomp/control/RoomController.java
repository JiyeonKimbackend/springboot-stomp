package com.example.stomp.control;

import com.example.stomp.domain.entity.ChatRoom;
import com.example.stomp.dto.BufferMap;
import com.example.stomp.dto.ChatMessageDto;
import com.example.stomp.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

    private final ChatService service;

    private final BufferMap<Long, List<ChatMessageDto>> chatListMap;

    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public ModelAndView rooms(){

        log.info("# All Chat Rooms");
        ModelAndView mv = new ModelAndView("chat/rooms");

        mv.addObject("list", service.findAllRooms());

        return mv;
    }

    //채팅방 개설
    @PostMapping(value = "/room")
    public String create(@RequestParam String name, HttpServletRequest request,  RedirectAttributes rttr){
        //채팅방 개설
        log.info("# Create Chat Room , name: " + name);
        ChatRoom chatRoom = service.createChatRoom(name);

        //파일 생성
        rttr.addFlashAttribute("roomName", chatRoom);
        String path = request.getServletContext().getRealPath("/chat");//src/main/webapp/chat
        service.chatRoomToFile(chatRoom, path);
        return "redirect:/chat/rooms";
    }

    //채팅방 조회
    @GetMapping("/room")
    public void getRoom(Long roomId, Model model){
        log.info("# get Chat Room, roomID : " + roomId);

        model.addAttribute("room", service.findRoomById(roomId));
    }

    //채팅방의 채팅내역 가져오기
    @GetMapping("/{roomId}")
    public ResponseEntity<List<String>> getChats(@PathVariable("roomId") Long roomId, HttpServletRequest request){
        List<ChatMessageDto> chatMesasge = chatListMap.get(roomId);

        if(chatMesasge != null && chatMesasge.size() > 0){
            /* Buffer의 내용 save 후 clear */
            service.saveChat(roomId, chatMesasge, request.getServletContext().getRealPath("/chat"));
            chatMesasge.clear();
        }
        List<String> chats = service.getChats(roomId, request.getServletContext().getRealPath("/chat"));

        return new ResponseEntity<>(chats, HttpStatus.OK);
    }
}
