package com.simzoo.withmedical.dto.filter;

import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Subject;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TutorFilterRequestDto {
    String gender;
    List<String> subjects;
    List<String> locations;
    List<String> universities;
    List<String> statusList;

    @Builder
    @Getter
    public static class TutorSearchFilter {
        Gender gender;
        List<Subject> subjects;
        List<String> locations;
        List<String> universities;
        List<EnrollmentStatus> statusList;
    }

    public Gender convertGender(java.lang.String gender) {
        if (gender == null || gender.isEmpty()) {
            return null;
        }
        return Gender.fromDescription(gender);
    }

    public List<Subject> convertSubjects(List<java.lang.String> subjectNames) {
        if (subjectNames == null || subjectNames.isEmpty()) {
            return null;
        }

        return subjectNames.stream()
            .map(Subject::fromDescription)
            .toList();
    }

    public List<EnrollmentStatus> convertEnrollmentStatus(List<java.lang.String> statusList) {
        if (statusList == null || statusList.isEmpty()) {
            return null;
        }

        return statusList.stream()
            .map(EnrollmentStatus::fromDescription)
            .toList();
    }
}
