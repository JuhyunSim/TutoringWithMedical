package com.simzoo.withmedical.repository.subject;

import com.simzoo.withmedical.entity.SubjectEntity;
import java.util.List;

public interface SubjectRepositoryCustom {
    List<SubjectEntity> fetchTutorSubjects(Long tutorProfileId);

    List<SubjectEntity> fetchTuteeSubjects(Long tuteeProfileId);
}
