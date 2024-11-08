package com.simzoo.withmedical.repository.chat.room;

import com.simzoo.withmedical.entity.chat.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long>, ChatRoomRepositoryCustom {
}
