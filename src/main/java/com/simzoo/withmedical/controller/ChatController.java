package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.chat.ChatMessageRequestDto;
import com.simzoo.withmedical.dto.chat.ChatMessageResponseDto;
import com.simzoo.withmedical.dto.chat.ChatRoomSimpleResponseDto;
import com.simzoo.withmedical.dto.chat.ChatStartRequestDto;
import com.simzoo.withmedical.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    /**
     * 채팅방 생성
     */
    @PostMapping("/chat/start")
    public ResponseEntity<Long> startChat(@RequestBody ChatStartRequestDto requestDto) {

        return ResponseEntity.ok(
            chatService.createChatRoom(requestDto.getSenderId(), requestDto.getRecipientId())
                .getId());
    }

    /**
     * 첫 채팅 메세지 전송
     */
    @PostMapping("/chat/{roomId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long roomId,
        @RequestBody ChatStartRequestDto requestDto) {
        // 메시지 저장 처리
        ChatMessageResponseDto chatMessage = chatService.sendMessage(roomId,
            requestDto.getSenderId(),
            requestDto.getRecipientId(), requestDto.getMessage()).toResponseDto();

        // 메시지 브로커로 메시지 전송
        simpMessagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);

        return ResponseEntity.ok("Message sent successfully");
    }

    /**
     * 채팅방 내에서 메세지 전송
     */
    @MessageMapping("/chat/{roomId}") //클라이언트가 "/app/chat"으로 보낸 메세지 처리
    public void processMessage(@DestinationVariable Long roomId, ChatMessageRequestDto requestDto) {

        //메세지 저장
        ChatMessageResponseDto chatMessage = chatService.sendChatMessage(roomId,
            requestDto.getSenderId(), requestDto.getMessage()).toResponseDto();

        simpMessagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);
    }

    //특정 사용자에게 보내기
    public void sendPrivateMessage(String userId, String message) {
        simpMessagingTemplate.convertAndSendToUser(userId, "/queue/reply", message);
    }

    /**
     * 채팅방 목록 조회
     */
    @GetMapping("/chatrooms")
    public ResponseEntity<Page<ChatRoomSimpleResponseDto>> getUserChatRooms(
        @RequestParam Long memberId,
        @PageableDefault(direction = Direction.DESC, sort = "updatedAt") Pageable pageable) {
        return ResponseEntity.ok(chatService.getUserChatRooms(memberId, pageable));
    }

    /**
     * 채팅방 대화내용 조회
     */
    @GetMapping("/chatrooms/{roomId}/messages")
    public ResponseEntity<Page<ChatMessageResponseDto>> getChatRoomMessages(
        @PathVariable Long roomId, @RequestParam Integer page, @RequestParam Integer size) {

        Pageable pageable = PageRequest.of(page, size,
            Sort.by(Direction.DESC, "createdAt"));

        return ResponseEntity.ok(chatService.getChatMessages(roomId, pageable));
    }


}
