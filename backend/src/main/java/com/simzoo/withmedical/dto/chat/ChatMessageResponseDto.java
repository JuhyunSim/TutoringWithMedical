package com.simzoo.withmedical.dto.chat;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ChatMessageResponseDto {
    Long senderId;
    String senderNickname;
    String message;

    @QueryProjection
    public ChatMessageResponseDto(Long senderId, String senderNickname, String message) {
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.message = message;
    }
}
