package com.simzoo.withmedical.repository;

import com.simzoo.withmedical.entity.TutorProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfileEntity, Long> {

}
