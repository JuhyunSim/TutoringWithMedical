package com.simzoo.withmedical.service;

import com.simzoo.withmedical.entity.ChatMessageEntity;
import com.simzoo.withmedical.entity.ChatRoomEntity;
import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.exception.CustomException;
import com.simzoo.withmedical.exception.ErrorCode;
import com.simzoo.withmedical.repository.ChatMessageRepository;
import com.simzoo.withmedical.repository.ChatRoomRepository;
import com.simzoo.withmedical.repository.MemberRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoomEntity createChatRoom(Long id1, Long id2) {
        Set<Long> memberIds = new HashSet<>(List.of(id1, id2));
        Map<Long, MemberEntity> members = memberRepository.findAllById(memberIds).stream()
            .collect(Collectors.toMap(MemberEntity::getId, memberEntity -> memberEntity));

        MemberEntity participant1 = members.get(id1);
        MemberEntity participant2 = members.get(id2);

        String title = String.format("%s(%s)와 %s(%s)의 대화",
            participant1.getNickname(),
            participant1.getRole().name(),
            participant2.getNickname(),
            participant2.getRole().name()
        );

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
            .participant1(participant1)
            .participant2(participant2)
            .title(title)
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
}
