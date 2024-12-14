package com.simzoo.withmedical.util;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.simzoo.withmedical.dto.SortRequestDto;
import com.simzoo.withmedical.entity.QReviewEntity;
import com.simzoo.withmedical.entity.QTuteePostEntity;
import com.simzoo.withmedical.entity.chat.QChatMessageEntity;
import com.simzoo.withmedical.entity.chat.QChatRoomEntity;
import com.simzoo.withmedical.enums.sort.TuteePostSortCriteria;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class OrderSpecifierUtil {

    //map으로 정렬할 entity 관리
    private static final Map<String, PathBuilder<?>> ENTITY_PATH_MAP = new HashMap<>();

    static {
        ENTITY_PATH_MAP.put("chatRoomEntity",
            new PathBuilder<>(QChatRoomEntity.class, "chatRoomEntity"));
        ENTITY_PATH_MAP.put("chatMessageEntity",
            new PathBuilder<>(QChatMessageEntity.class, "chatMessageEntity"));
        ENTITY_PATH_MAP.put("reviewEntity", new PathBuilder<>(QReviewEntity.class, "reviewEntity"));
        ENTITY_PATH_MAP.put("tuteePostEntity",
            new PathBuilder<>(QTuteePostEntity.class, "tuteePostEntity"));
        // 새로운 엔티티가 추가될 때마다 여기에 추가
    }

    public static OrderSpecifier<?>[] getAllOrderSpecifiers(Pageable pageable, String entityType) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            PathBuilder<?> entityPath = ENTITY_PATH_MAP.get(entityType);

            if (entityPath == null) {
                throw new IllegalArgumentException("Entity의 타입이 아닙니다.");
            }

            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                Expression fieldPath = Expressions.path(Object.class, entityPath,
                    order.getProperty());
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(direction, fieldPath);
                orders.add(orderSpecifier);
            }
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

    public static OrderSpecifier<?>[] getAllOrderSpecifiers(
        List<SortRequestDto<TuteePostSortCriteria>> sortRequests,
        Pageable pageable,
        String entityType,
        String defaultSortField,
        Order defaultSortDirection
    ) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        PathBuilder<?> entityPath = ENTITY_PATH_MAP.get(entityType);
        if (entityPath == null) {
            throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }

        // 1. SortRequests 기반 정렬 생성
        if (sortRequests != null && !sortRequests.isEmpty()) {
            for (SortRequestDto<TuteePostSortCriteria> sortRequest : sortRequests) {
                String field = sortRequest.getSortBy() == null ? defaultSortField : sortRequest.getSortBy()
                    .getField();
                // 복합 경로 처리

                Order direction = Optional.ofNullable(sortRequest.getDirection())
                    .map(Direction::isAscending)
                    .map(isAscending -> isAscending ? Order.ASC : Order.DESC)
                    .orElse(Order.DESC);

                Expression fieldPath = createFieldPath(entityPath, field);
                orders.add(new OrderSpecifier<>(direction, fieldPath));
            }
        }

        // 2. Pageable의 Sort 사용 (sortRequests가 비어 있을 때만)
        if (orders.isEmpty() && !isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                Expression fieldPath = Expressions.path(Object.class, entityPath, order.getProperty());
                orders.add(new OrderSpecifier<>(direction, fieldPath));
            }
        }

        // 3. 기본 정렬 추가
        if (orders.isEmpty()) {
            Expression defaultFieldPath = Expressions.path(Object.class, entityPath, defaultSortField);
            orders.add(new OrderSpecifier<>(defaultSortDirection, defaultFieldPath));
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

    private static boolean isEmpty(Sort sort) {
        return sort == null || !sort.iterator().hasNext();
    }

    private static Expression<?> createFieldPath(PathBuilder<?> entityPath, String sortBy) {
        String[] parts = sortBy.split("\\.");
        Expression<?> fieldPath = entityPath;
        for (String part : parts) {
            fieldPath = Expressions.path(fieldPath.getType(), (Path<?>) fieldPath, part);
        }
        return fieldPath;
    }
}
