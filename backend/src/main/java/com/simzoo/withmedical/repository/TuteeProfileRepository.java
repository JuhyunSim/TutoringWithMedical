package com.simzoo.withmedical.repository;

import com.simzoo.withmedical.entity.TuteeProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuteeProfileRepository extends JpaRepository<TuteeProfileEntity, Long> {

}
