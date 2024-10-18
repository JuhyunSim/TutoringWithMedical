package com.simzoo.withmedical.repository.chat.room;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.chat.ChatRoomSimpleResponseDto;
import com.simzoo.withmedical.dto.chat.QChatRoomSimpleResponseDto;
import com.simzoo.withmedical.entity.QChatMessageEntity;
import com.simzoo.withmedical.entity.QChatRoomEntity;
import com.simzoo.withmedical.util.OrderSpecifierUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatRoomSimpleResponseDto> findAllChatRoomsCreatedByMe(Long userId,
        Pageable pageable) {
        QChatRoomEntity chatRoom = QChatRoomEntity.chatRoomEntity;
        QChatMessageEntity chatMessage = QChatMessageEntity.chatMessageEntity;

        // 쿼리 작성
        List<ChatRoomSimpleResponseDto> results = queryFactory
            .select(new QChatRoomSimpleResponseDto(
                chatRoom.id,
                chatRoom.title,
                chatMessage.message,
                chatRoom.updatedAt))
            .from(chatRoom)
            .leftJoin(chatMessage).on(chatRoom.id.eq(chatMessage.chatRoom.id))
            .where(chatRoom.createdBy.eq(userId))  // createdBy가 userId와 일치하는 채팅방
            .where(chatMessage.createdAt.in(
                queryFactory.select(chatMessage.createdAt.max())
                    .from(chatMessage)
                    .groupBy(chatMessage.chatRoom.id)
            ))
            .orderBy(OrderSpecifierUtil.getAllOrderSpecifiers(pageable,
                "chatRoomEntity"))  // 최근 업데이트 순으로 정렬
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 전체 개수 가져오기
        long total = queryFactory
            .select(chatRoom.count())
            .from(chatRoom)
            .where(chatRoom.createdBy.eq(userId))
            .fetchOne();

        // Page 객체 반환
        return new PageImpl<>(results, pageable, total);
    }
}
