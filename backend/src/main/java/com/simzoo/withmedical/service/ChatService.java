package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.chat.ChatMessageResponseDto;
import com.simzoo.withmedical.dto.chat.ChatRoomSimpleResponseDto;
import com.simzoo.withmedical.entity.ChatMessageEntity;
import com.simzoo.withmedical.entity.ChatRoomEntity;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.chat.message.ChatMessageRepository;
import com.simzoo.withmedical.repository.chat.room.ChatRoomRepository;
import com.simzoo.withmedical.repository.MemberRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoomEntity createChatRoom(Long senderId, Long recipientId) {
        Set<Long> memberIds = new HashSet<>(List.of(senderId, recipientId));
        Map<Long, MemberEntity> members = memberRepository.findAllById(memberIds).stream()
            .collect(Collectors.toMap(MemberEntity::getId, memberEntity -> memberEntity));

        MemberEntity participant1 = members.get(senderId);
        MemberEntity participant2 = members.get(recipientId);

        String title = String.format("%s(%s) & %s(%s)의 대화",
            participant1.getNickname(),
            participant1.getRole().name(),
            participant2.getNickname(),
            participant2.getRole().name()
        );

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
            .participant1(participant1)
            .participant2(participant2)
            .title(title)
            .createdBy(participant1.getId())
            .build();
        return chatRoomRepository.save(chatRoomEntity);
    }

    @Transactional
    public ChatMessageEntity sendMessage(Long roomId, Long senderId, Long recipientId,
        String messageContent) {

        Set<Long> memberIds = new HashSet<>(List.of(senderId, recipientId));
        Map<Long, MemberEntity> members = memberRepository.findAllById(memberIds).stream()
            .collect(Collectors.toMap(MemberEntity::getId, memberEntity -> memberEntity));

        MemberEntity sender = members.get(senderId);
        MemberEntity recipient = members.get(recipientId);

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        ChatMessageEntity chatMessage = ChatMessageEntity.builder()
            .sender(sender)
            .recipient(recipient)
            .message(messageContent)
            .chatRoom(chatRoomEntity)
            .build();

        return chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public ChatMessageEntity sendChatMessage(Long roomId, Long senderId, String messageContent) {

        MemberEntity sender = memberRepository.findById(senderId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        ChatMessageEntity chatMessage = ChatMessageEntity.builder()
            .sender(sender)
            .message(messageContent)
            .chatRoom(chatRoomEntity)
            .build();

        return chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public Page<ChatRoomSimpleResponseDto> getUserChatRooms(Long memberId, Pageable pageable) {
        return chatRoomRepository.findAllChatRoomsCreatedByMe(memberId, pageable);
    }

    @Transactional
    public Page<ChatMessageResponseDto> getChatMessages(Long roomId, Pageable pageable) {
        return chatMessageRepository.findMessagesByRoomId(roomId, pageable);
    }
}
