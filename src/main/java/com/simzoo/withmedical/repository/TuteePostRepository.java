package com.simzoo.withmedical.repository;

import com.simzoo.withmedical.entity.TuteePostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuteePostRepository extends JpaRepository<TuteePostEntity, Long> {

}
