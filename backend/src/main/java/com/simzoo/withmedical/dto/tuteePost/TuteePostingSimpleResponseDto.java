package com.simzoo.withmedical.dto.tuteePost;

import com.querydsl.core.annotations.QueryProjection;
import com.simzoo.withmedical.enums.Gender;
import com.simzoo.withmedical.enums.TuteeGrade;
import com.simzoo.withmedical.enums.TutoringType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class TuteePostingSimpleResponseDto {

    private Long postingId;
    private Long memberId;
    private String memberNickname;
    private Gender gender;
    private TuteeGrade studentGrade;
    private String studentSchool;
    private String personality;
    private TutoringType tutoringType;
    private String possibleSchedule;
    private String level;
    private Integer fee;

    @QueryProjection
    public TuteePostingSimpleResponseDto(Long postingId, Long memberId, String memberNickname,
        Gender gender, TuteeGrade studentGrade, String studentSchool, String personality,
        TutoringType tutoringType, String possibleSchedule, String level, Integer fee) {

        this.postingId = postingId;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.gender = gender;
        this.studentGrade = studentGrade;
        this.studentSchool = studentSchool;
        this.personality = personality;
        this.tutoringType = tutoringType;
        this.possibleSchedule = possibleSchedule;
        this.level = level;
        this.fee = fee;
    }
}
