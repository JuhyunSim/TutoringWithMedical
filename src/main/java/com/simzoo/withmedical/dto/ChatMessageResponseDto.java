package com.simzoo.withmedical.dto;

import com.simzoo.withmedical.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {
    Long senderId;
    Long recipientId;
    String senderNickname;
    String recipientNickname;
    Role senderRole;
    Role recipientRole;
    String message;
}
