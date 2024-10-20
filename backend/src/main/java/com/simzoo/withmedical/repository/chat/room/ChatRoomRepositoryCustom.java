package com.simzoo.withmedical.repository.chat.room;

import com.simzoo.withmedical.dto.chat.ChatRoomSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {
    Page<ChatRoomSimpleResponseDto> findAllChatRoomsCreatedByMe(Long userId, Pageable pageable);
}
