package com.simzoo.withmedical.repository.chat.room;

import com.simzoo.withmedical.entity.chat.ChatRoomMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    Long countByChatRoomId(Long chatRoomId);
}
