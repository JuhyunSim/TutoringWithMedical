package com.simzoo.withmedical.dto;

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
    private Long member1Id;
    private Long member2Id;
}
