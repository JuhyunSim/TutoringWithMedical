package com.simzoo.withmedical.repository.chat.message;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.chat.ChatMessageResponseDto;
import com.simzoo.withmedical.dto.chat.QChatMessageResponseDto;
import com.simzoo.withmedical.entity.chat.QChatMessageEntity;
import com.simzoo.withmedical.util.OrderSpecifierUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatMessageResponseDto> findMessagesByRoomId(Long roomId, Pageable pageable) {

        QChatMessageEntity chatMessage = QChatMessageEntity.chatMessageEntity;

        List<ChatMessageResponseDto> list = queryFactory.select(
                new QChatMessageResponseDto(
                    chatMessage.sender.id,
                    chatMessage.sender.nickname,
                    chatMessage.message))
            .from(chatMessage)
            .where(chatMessage.chatRoom.id.eq(roomId))
            .orderBy(OrderSpecifierUtil.getAllOrderSpecifiers(pageable,
                "chatMessageEntity"))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory.select(chatMessage.count())
            .from(chatMessage)
            .where(chatMessage.chatRoom.id.eq(roomId))
            .fetchOne();


        return new PageImpl<>(list, pageable, total);
    }
}
