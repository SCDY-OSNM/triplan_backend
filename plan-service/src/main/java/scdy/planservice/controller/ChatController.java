package scdy.planservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import scdy.planservice.common.exceptions.ForbiddenException;
import scdy.planservice.dto.ChatMessageDto;
import scdy.planservice.service.ChatService;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final SimpMessagingTemplate messagingTemplate;

    // 채팅방 참가
    @MessageMapping("/chat/join.{roomId}") // 해당 주소로 메세지를 전달
    public void joinChat(@DestinationVariable("roomId") String roomId, ChatMessageDto messageDto){
        chatService.addMember(roomId, messageDto.getMessageSenderId());

        messageDto.setMessageType(ChatMessageDto.MessageType.JOIN);
        messageDto.setMessage(messageDto.getMessageSenderId() + "가 채팅에 참가했습니다");

        messagingTemplate.convertAndSend("/sub/chat/room." + roomId, messageDto);

    }

    // 채팅 전송
    @MessageMapping("/chat/send/{roomId}")
    public ChatMessageDto sendMessage(@DestinationVariable("roomId") String roomId, ChatMessageDto messageDto){
        if(!chatService.isMember(roomId, messageDto.getMessageSenderId())){
            throw new ForbiddenException("채팅방의 멤버가 아닙니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room." + roomId, messageDto);

        return messageDto;
    }
}
