package com.simzoo.withmedical.dto.chat;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class ChatRoomSimpleResponseDto {
    private Long roomId;
    private String title;
    private String lastMessage;
    private LocalDateTime updatedAt;

    @QueryProjection
    public ChatRoomSimpleResponseDto(Long roomId, String title, String lastMessage, LocalDateTime updatedAt) {
        this.roomId = roomId;
        this.title = title;
        this.lastMessage = lastMessage;
        this.updatedAt = updatedAt;
    }
}
