package com.simzoo.withmedical.repository.review;

import com.simzoo.withmedical.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>, ReviewRepositoryCustom {

}
