package com.simzoo.withmedical.repository.chat.message;

import com.simzoo.withmedical.dto.chat.ChatMessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageRepositoryCustom {
    Page<ChatMessageResponseDto> findMessagesByRoomId(Long roomId, Pageable pageable);
}
