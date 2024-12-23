package com.simzoo.withmedical.dto.member;

import com.simzoo.withmedical.dto.location.LocationDto;
import com.simzoo.withmedical.dto.school.SchoolDto;
import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class UpdateMemberRequestDto {
    private String nickname;
    private Gender gender;
    private UpdateTutorProfileRequestDto tutorProfile;
    private UpdateTuteeProfileRequestDto tuteeProfile;

    @Getter
    @Builder
    public static class UpdateTutorProfileRequestDto {
        private String proofFileUrl;
        private String imageUrl;
        private List<Subject> subjects;
        private LocationDto location;
        private String description;
        private SchoolDto university;
        private EnrollmentStatus status;
    }

    @Getter
    @Builder
    public static class UpdateTuteeProfileRequestDto {
        private Long tuteeId;
        private String tuteeName;
        private Gender gender;
        @NotNull
        private List<Subject> subjects;
        private LocationDto location;
        private TuteeGrade tuteeGrade;
        private String description;
    }
}


