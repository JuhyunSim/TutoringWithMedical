package com.simzoo.withmedical.dto.filter;

import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
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
    public static class TutorEnumFilter {
        Gender gender;
        List<Subject> subjects;
        List<Location> locations;
        List<University> universities;
        List<EnrollmentStatus> statusList;
    }

    public Gender convertGender(String gender) {
        if (gender == null || gender.isEmpty()) {
            return null;
        }
        return Gender.fromDescription(gender);
    }

    public List<Subject> convertSubjects(List<String> subjectNames) {
        if (subjectNames == null || subjectNames.isEmpty()) {
            return null;
        }

        return subjectNames.stream()
            .map(Subject::fromDescription)
            .toList();
    }

    public List<Location> convertLocations(List<String> locations) {
        if (locations == null || locations.isEmpty()) {
            return null;
        }

        return locations.stream()
            .map(Location::fromDescription)
            .toList();
    }

    public List<University> convertUniversity(List<String> universities) {
        if (universities == null || universities.isEmpty()) {
            return null;
        }

        return universities.stream()
            .map(University::fromDescription)
            .toList();
    }

    public List<EnrollmentStatus> convertEnrollmentStatus(List<String> statusList) {
        if (statusList == null || statusList.isEmpty()) {
            return null;
        }

        return statusList.stream()
            .map(EnrollmentStatus::fromDescription)
            .toList();
    }
}
