package com.simzoo.withmedical.repository;

import com.simzoo.withmedical.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    List<MemberEntity> findAllById(Iterable<Long> ids);

    Optional<MemberEntity> findByTutorProfile_Id(Long tutorProfileId);

    Optional<MemberEntity> findByPhoneNumber(String phoneNumber);
}
