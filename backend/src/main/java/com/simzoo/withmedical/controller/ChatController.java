package com.simzoo.withmedical.controller;

import com.simzoo.withmedical.dto.chat.ChatMessageRequestDto;
import com.simzoo.withmedical.dto.chat.ChatMessageResponseDto;
import com.simzoo.withmedical.dto.chat.ChatRoomExistDto;
import com.simzoo.withmedical.dto.chat.ChatRoomSimpleResponseDto;
import com.simzoo.withmedical.dto.chat.ChatStartRequestDto;
import com.simzoo.withmedical.entity.chat.ChatRoomEntity;
import com.simzoo.withmedical.enums.filter.ChatRoomFilterType;
import com.simzoo.withmedical.service.ChatService;
import com.simzoo.withmedical.util.resolver.LoginId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @PostMapping("/chat/start-and-send")
    public ResponseEntity<ChatRoomSimpleResponseDto> startAndSendMessage(
        @LoginId Long senderId,
        @RequestBody ChatStartRequestDto requestDto) {

        // 채팅방 생성
        ChatRoomEntity chatRoom = chatService.createChatRoom(senderId, requestDto.getRecipientId());

        // 첫 메시지 전송
        ChatMessageResponseDto chatMessage = chatService.sendMessage(chatRoom.getId(), senderId,
                requestDto.getRecipientId(), requestDto.getMessage())
            .toResponseDto();

        // 메시지 브로커로 메시지 전송
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatRoom.getId(), chatMessage);

        // 채팅방 정보 반환
        return ResponseEntity.ok(
            ChatRoomSimpleResponseDto.builder()
                .roomId(chatRoom.getId())
                .title(chatRoom.getTitle())
                .lastMessage(chatMessage.getMessage())
                .updatedAt(chatRoom.getUpdatedAt())
                .build()
        );
    }

    /**
     * 채팅방 내에서 메세지 전송
     */
    @MessageMapping("/chat/{roomId}") //클라이언트가 "/app/chat"으로 보낸 메세지 처리
    public void processMessage(@DestinationVariable Long roomId, ChatMessageRequestDto requestDto,
        SimpMessageHeaderAccessor headerAccessor) {

        Long senderId = (Long) headerAccessor.getSessionAttributes().get("userId");

        //메세지 저장
        ChatMessageResponseDto chatMessage = chatService.sendChatMessage(roomId,
            senderId, requestDto.getMessage()).toResponseDto();

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
        @LoginId Long memberId,
        @RequestParam(defaultValue = "ALL") ChatRoomFilterType filterType,
        @PageableDefault(direction = Direction.DESC, sort = "updatedAt") Pageable pageable) {
        log.info("memberId: {}", memberId);
        return ResponseEntity.ok(chatService.getUserChatRooms(memberId, filterType, pageable));
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

    @DeleteMapping("/chatrooms/{roomId}")
    public ResponseEntity<Void> exit(@LoginId Long memberId, @PathVariable Long roomId) {

        chatService.exitChatroom(memberId, roomId);

        return ResponseEntity.ok().build();
    }

    /**
     * 채팅방 중복체크
     */
    @GetMapping("/chatrooms/exist")
    public ResponseEntity<ChatRoomExistDto> checkDuplication(@LoginId Long senderId,
        @RequestParam Long recipientId) {
        ChatRoomExistDto chatRoomExistDto = chatService.checkRoomExist(senderId, recipientId);

        return ResponseEntity.ok(chatRoomExistDto);
    }
}
