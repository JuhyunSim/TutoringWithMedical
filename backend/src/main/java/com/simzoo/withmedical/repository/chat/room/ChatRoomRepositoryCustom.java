package com.simzoo.withmedical.repository.chat.room;

import com.simzoo.withmedical.dto.chat.ChatRoomSimpleResponseDto;
import com.simzoo.withmedical.entity.chat.ChatRoomEntity;
import com.simzoo.withmedical.enums.filter.ChatRoomFilterType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {

    Page<ChatRoomSimpleResponseDto> findAllChatRoomsCreatedByMe(Long userId,
        ChatRoomFilterType filterTp, Pageable pageable);

    Optional<ChatRoomEntity> findByIdWithMember(Long id, Long userId);
}
