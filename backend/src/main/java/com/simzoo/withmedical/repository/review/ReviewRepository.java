package com.simzoo.withmedical.repository.review;

import com.simzoo.withmedical.entity.ReviewEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>,
    ReviewRepositoryCustom {

    Optional<ReviewEntity> findByIdAndWriter_Id(Long id, Long writerId);

    Optional<Object> findByTutorProfile_IdAndWriter_Id(Long tutorProfileId, Long userId);
}
