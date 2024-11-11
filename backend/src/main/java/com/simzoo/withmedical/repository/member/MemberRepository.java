package com.simzoo.withmedical.repository.member;

import com.simzoo.withmedical.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberRepositoryCustom {
    List<MemberEntity> findAllById(Iterable<Long> ids);

    Optional<MemberEntity> findByTutorProfile_Id(Long tutorProfileId);

    Optional<MemberEntity> findByHashedPhoneNumber(String phoneNumber);
}
