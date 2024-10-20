package com.simzoo.withmedical.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class ChatStartRequestDto {
    private Long senderId;
    private Long recipientId;
    private String message;
}
