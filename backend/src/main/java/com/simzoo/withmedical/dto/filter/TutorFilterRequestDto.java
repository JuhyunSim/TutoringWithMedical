package com.simzoo.withmedical.dto.filter;

import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.University;
import com.simzoo.withmedical.enums.Subject;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TutorFilterRequestDto {
    java.lang.String gender;
    List<java.lang.String> subjects;
    List<java.lang.String> locations;
    List<java.lang.String> universities;
    List<java.lang.String> statusList;

    @Builder
    @Getter
    public static class TutorEnumFilter {
        Gender gender;
        List<Subject> subjects;
        List<Location> locations;
        List<University> universities;
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

    public List<Location> convertLocations(List<java.lang.String> locations) {
        if (locations == null || locations.isEmpty()) {
            return null;
        }

        return locations.stream()
            .map(Location::fromDescription)
            .toList();
    }

    public List<University> convertUniversity(List<java.lang.String> universities) {
        if (universities == null || universities.isEmpty()) {
            return null;
        }

        return universities.stream()
            .map(University::fromDescription)
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
