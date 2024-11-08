package com.simzoo.withmedical.repository.chat.message;

import com.simzoo.withmedical.entity.chat.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long>, ChatMessageRepositoryCustom {
    void deleteAllByChatRoomId(Long chatRoomId);
}
