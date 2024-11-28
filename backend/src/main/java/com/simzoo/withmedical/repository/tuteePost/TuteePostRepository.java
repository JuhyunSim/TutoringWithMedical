package com.simzoo.withmedical.repository.tuteePost;

import com.simzoo.withmedical.entity.TuteePostEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuteePostRepository extends JpaRepository<TuteePostEntity, Long>, TuteePostRepositoryCustom {

    Page<TuteePostEntity> findAllByMember_Id(Long myId, Pageable pageable);

    Optional<TuteePostEntity> findByIdAndMember_Id(Long postingId, Long memberId);
}
