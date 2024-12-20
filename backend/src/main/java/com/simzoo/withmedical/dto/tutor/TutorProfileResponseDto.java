package com.simzoo.withmedical.dto.tutor;

import com.querydsl.core.annotations.QueryProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class TutorProfileResponseDto {

    private Long tutorId;
    private String nickname;
    private String gender;
    private String imageUrl;
    private List<String> subjects = new ArrayList<>();
    private String location;
    private String univName;
    private String status;
    private String description;

    @QueryProjection
    public TutorProfileResponseDto(Long tutorId, String nickname, String gender, String imageUrl,
        List<String> subjects,
        String location, String univName, String status,
        String description) {
        this.tutorId = tutorId;
        this.nickname = nickname;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.subjects = subjects;
        this.location = location;
        this.univName = univName;
        this.status = status;
        this.description = description;
    }
}
