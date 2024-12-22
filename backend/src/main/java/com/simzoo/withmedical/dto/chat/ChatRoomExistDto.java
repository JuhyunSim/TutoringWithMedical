package com.simzoo.withmedical.dto.chat;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomExistDto {
    private Long roomId;

    @QueryProjection
    public ChatRoomExistDto(Long roomId) {
        this.roomId = roomId;
    }
}
