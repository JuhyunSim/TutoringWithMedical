package com.simzoo.withmedical.repository.chat.room;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simzoo.withmedical.dto.chat.ChatRoomSimpleResponseDto;
import com.simzoo.withmedical.dto.chat.QChatRoomSimpleResponseDto;
import com.simzoo.withmedical.entity.chat.ChatRoomEntity;
import com.simzoo.withmedical.entity.chat.QChatMessageEntity;
import com.simzoo.withmedical.entity.chat.QChatRoomEntity;
import com.simzoo.withmedical.entity.chat.QChatRoomMember;
import com.simzoo.withmedical.enums.filter.ChatRoomFilterType;
import com.simzoo.withmedical.util.OrderSpecifierUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Slf4j
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatRoomSimpleResponseDto> findAllChatRoomsCreatedByMe(Long userId,
        ChatRoomFilterType filterType, Pageable pageable) {
        QChatRoomEntity chatRoom = QChatRoomEntity.chatRoomEntity;
        QChatMessageEntity chatMessage = QChatMessageEntity.chatMessageEntity;
        QChatRoomMember chatRoomMember = QChatRoomMember.chatRoomMember;

        // 쿼리 작성
        List<ChatRoomSimpleResponseDto> results = queryFactory
            .select(new QChatRoomSimpleResponseDto(
                chatRoom.id,
                chatRoom.title,
                chatMessage.message,
                chatRoom.updatedAt))
            .from(chatRoom)
            .leftJoin(chatRoomMember).on(chatRoomMember.chatRoom.id.eq(chatRoom.id))
            .leftJoin(chatMessage).on(chatRoom.id.eq(chatMessage.chatRoom.id)
                .and(chatMessage.createdAt.eq(
                    JPAExpressions.select(chatMessage.createdAt.max())
                        .from(chatMessage)
                        .where(chatMessage.chatRoom.id.eq(chatRoom.id))
                )))
            .where(getFilterCondition(chatRoom, chatRoomMember, userId, filterType))
            .orderBy(OrderSpecifierUtil.getAllOrderSpecifiers(pageable,
                "chatRoomEntity"))  // 최근 업데이트 순으로 정렬
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .distinct()  // 여기서 중복 제거
            .fetch();

        // 전체 개수 가져오기
        long total = queryFactory
            .select(chatRoom.count())
            .from(chatRoom)
            .leftJoin(chatRoomMember).on(chatRoomMember.chatRoom.id.eq(chatRoom.id))
            .where(getFilterCondition(chatRoom, chatRoomMember, userId, filterType))
            .fetchOne();

        // Page 객체 반환
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Optional<ChatRoomEntity> findByIdWithMember(Long id, Long userId) {

        QChatRoomEntity chatRoom = QChatRoomEntity.chatRoomEntity;
        QChatRoomMember chatRoomMember = QChatRoomMember.chatRoomMember;

        ChatRoomEntity result = queryFactory
            .selectFrom(chatRoom)
            .join(chatRoom.members, chatRoomMember)
            .where(chatRoom.id.eq(id)
                .and(chatRoomMember.member.id.eq(userId)))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression getFilterCondition(QChatRoomEntity chatRoom,
        QChatRoomMember chatRoomMember, Long userId,
        ChatRoomFilterType filterType) {

        switch (filterType) {
            case CREATED_BY_ME:
                return chatRoom.createdBy.eq(userId).and(chatRoomMember.member.id.eq(userId));
            case INVITED:
                return chatRoom.createdBy.ne(userId)
                    .and(chatRoomMember.member.id.eq(userId));
            case ALL:
            default:
                return chatRoomMember.member.id.eq(userId);
        }
    }
}
