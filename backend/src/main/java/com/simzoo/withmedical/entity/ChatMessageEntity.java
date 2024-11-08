package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.chat.ChatMessageResponseDto;
import com.simzoo.withmedical.enums.MessageType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity(name = "chatMessage")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@AuditOverride(forClass = BaseEntity.class)
public class ChatMessageEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoomEntity chatRoom;

    private String message;

    private MessageType messageType;

    public ChatMessageResponseDto toResponseDto() {
        return ChatMessageResponseDto.builder()
            .senderId(sender.getId())
            .message(message)
            .senderNickname(sender.getNickname())
            .build();
    }
}
