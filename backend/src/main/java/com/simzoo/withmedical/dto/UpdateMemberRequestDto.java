package com.simzoo.withmedical.dto;

import com.simzoo.withmedical.enums.EnrollmentStatus;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.enums.University;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class UpdateMemberRequestDto {
    private String nickname;
    private UpdateTutorProfileRequestDto tutorProfile;
    private UpdateTuteeProfileRequestDto tuteeProfile;

    @Getter
    @Builder
    public static class UpdateTutorProfileRequestDto {
        private MultipartFile proofFile;
        private MultipartFile profileImage;
        private List<Subject> subjects;
        private Location location;
        private String description;
        private University university;
        private EnrollmentStatus status;
    }

    @Getter
    public static class UpdateTuteeProfileRequestDto {
        private List<Subject> subjects = new ArrayList<>();
        private Location location;
        private TuteeGrade tuteeGrade;
        private String description;
    }
}


