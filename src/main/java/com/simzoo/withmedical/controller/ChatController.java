package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.ChatMessageResponseDto;
import com.simzoo.withmedical.dto.ChatStartRequestDto;
import com.simzoo.withmedical.dto.ChatMessageRequestDto;
import com.simzoo.withmedical.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @PostMapping("/chat/start")
    public ResponseEntity<?> startChat(@RequestBody ChatStartRequestDto requestDto) {

        return ResponseEntity.ok(
            chatService.createChatRoom(requestDto.getMember1Id(), requestDto.getMember2Id()).getId());
    }

    @PostMapping("/chat/{roomId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long roomId, @RequestBody ChatMessageRequestDto requestDto) {
        // 메시지 저장 처리
        ChatMessageResponseDto chatMessage = chatService.sendMessage(roomId, requestDto.getSenderId(),
            requestDto.getRecipientId(), requestDto.getMessage()).toResponseDto();

        // 메시지 브로커로 메시지 전송
        simpMessagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);

        return ResponseEntity.ok("Message sent successfully");
    }

    @MessageMapping("/chat/{roomId}") //클라이언트가 "/app/chat"으로 보낸 메세지 처리
    public void processMessage(@DestinationVariable Long roomId, ChatMessageRequestDto requestDto) {

        //메세지 저장
        ChatMessageResponseDto chatMessage = chatService.sendMessage(roomId, requestDto.getSenderId(),
            requestDto.getRecipientId(), requestDto.getMessage()).toResponseDto();

        simpMessagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);
    }

    //특정 사용자에게 보내기
    public void sendPrivateMessage(String userId, String message) {
        simpMessagingTemplate.convertAndSendToUser(userId, "/queue/reply", message);
    }
}
