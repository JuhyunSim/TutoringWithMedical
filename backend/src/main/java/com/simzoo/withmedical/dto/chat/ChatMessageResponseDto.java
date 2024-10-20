package com.simzoo.withmedical.dto.chat;

import com.querydsl.core.annotations.QueryProjection;
import com.simzoo.withmedical.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ChatMessageResponseDto {
    Long senderId;
    String senderNickname;
    Role senderRole;
    String message;

    @QueryProjection
    public ChatMessageResponseDto(Long senderId, String senderNickname, Role senderRole, String message) {
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.senderRole = senderRole;
        this.message = message;
    }
}
