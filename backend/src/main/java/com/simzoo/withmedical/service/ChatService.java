package com.simzoo.withmedical.service;

import com.simzoo.withmedical.dto.chat.ChatMessageResponseDto;
import com.simzoo.withmedical.dto.chat.ChatRoomSimpleResponseDto;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.chat.ChatMessageEntity;
import com.simzoo.withmedical.entity.chat.ChatRoomEntity;
import com.simzoo.withmedical.entity.chat.ChatRoomMember;
import com.simzoo.withmedical.enums.filter.ChatRoomFilterType;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.MemberRepository;
import com.simzoo.withmedical.repository.chat.message.ChatMessageRepository;
import com.simzoo.withmedical.repository.chat.room.ChatRoomMemberRepository;
import com.simzoo.withmedical.repository.chat.room.ChatRoomRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public ChatRoomEntity createChatRoom(Long senderId, Long recipientId) {
        Map<Long, MemberEntity> members = getMemberEntityMap(senderId, recipientId);

        MemberEntity participant1 = members.get(senderId);
        MemberEntity participant2 = members.get(recipientId);

        String title = String.format("%s & %s의 대화",
            participant1.getNickname(),
            participant2.getNickname()
        );

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
            .title(title)
            .createdBy(participant1.getId())
            .build();

        chatRoomRepository.save(chatRoomEntity);

        chatRoomMemberRepository.save(addMemberToChatRoom(participant1, chatRoomEntity));
        chatRoomMemberRepository.save(addMemberToChatRoom(participant2, chatRoomEntity));

        return chatRoomEntity;
    }

    @Transactional
    public ChatMessageEntity sendMessage(Long roomId, Long senderId, Long recipientId,
        String messageContent) {
        log.info("senderId: {}, recipientId: {}", senderId, recipientId);

        Map<Long, MemberEntity> members = getMemberEntityMap(senderId, recipientId);

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

    @Transactional(readOnly = true)
    public Page<ChatRoomSimpleResponseDto> getUserChatRooms(Long memberId,
        ChatRoomFilterType filterType, Pageable pageable) {
        return chatRoomRepository.findAllChatRoomsCreatedByMe(memberId, filterType, pageable);

    }

    @Transactional
    public Page<ChatMessageResponseDto> getChatMessages(Long roomId, Pageable pageable) {
        return chatMessageRepository.findMessagesByRoomId(roomId, pageable);
    }

    @Transactional
    public void exitChatroom(Long memberId, Long roomId) {

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(roomId,
            memberId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM_MEMBER));

        chatRoomMemberRepository.delete(chatRoomMember);
        log.info("{} 님이 {} 채팅방에서 퇴장하셨습니다.", memberId, roomId);

        if (chatRoomMemberRepository.countByChatRoomId(roomId) == 0) {
           chatRoomRepository.deleteById(roomId);
            log.info("채팅방 {}이(가) 모든 회원이 퇴장하여 삭제되었습니다.", roomId);
            chatMessageRepository.deleteAllByChatRoomId(roomId);
        }
    }

    private Map<Long, MemberEntity> getMemberEntityMap(Long senderId,
        Long recipientId) {
        Set<Long> memberIds = new HashSet<>(List.of(senderId, recipientId));
        Map<Long, MemberEntity> members = memberRepository.findAllById(memberIds).stream()
            .collect(Collectors.toMap(MemberEntity::getId, memberEntity -> memberEntity));
        return members;
    }

    private ChatRoomMember addMemberToChatRoom(MemberEntity participant1,
        ChatRoomEntity chatRoomEntity) {
        return ChatRoomMember.builder()
            .member(participant1)
            .chatRoom(chatRoomEntity)
            .build();
    }
}
