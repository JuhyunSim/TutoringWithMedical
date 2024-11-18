package com.simzoo.withmedical.repository;

import com.simzoo.withmedical.entity.TuteeProfileEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuteeProfileRepository extends JpaRepository<TuteeProfileEntity, Long> {

    Optional<TuteeProfileEntity> findByIdAndMember_Id(Long tuteeId, Long userId);
}
