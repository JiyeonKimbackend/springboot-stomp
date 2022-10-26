package com.example.stomp.control;

import com.example.stomp.domain.entity.ChatRoom;
import com.example.stomp.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

    private final ChatService service;

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
}
