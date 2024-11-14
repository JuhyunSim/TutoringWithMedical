package com.simzoo.withmedical.repository.subject;

import com.simzoo.withmedical.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long>, SubjectRepositoryCustom {

    void deleteAllByTuteeId(Long tuteeId);
}
