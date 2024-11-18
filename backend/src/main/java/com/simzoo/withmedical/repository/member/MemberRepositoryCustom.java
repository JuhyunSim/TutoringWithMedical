package com.simzoo.withmedical.repository.member;

import com.simzoo.withmedical.entity.MemberEntity;
import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<MemberEntity> findByIdWithProfile(Long memberId);
    Optional<MemberEntity> findParentWithATuteeProfile(Long memberId);
}

