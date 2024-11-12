package com.simzoo.withmedical.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.simzoo.withmedical.enums.Location;
import com.simzoo.withmedical.enums.Subject;
import com.simzoo.withmedical.enums.University;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TutorSimpleResponseDto {

    private Long id;
    private String imageUrl;
    private String nickname;
    private University university;
    private Location location;
    private List<Subject> subjects;

    @QueryProjection
    public TutorSimpleResponseDto(Long id, String imageUrl, String nickname, University university, Location location,
        List<Subject> subjects) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.university = university;
        this.location = location;
        this.subjects = subjects;
    }
}
