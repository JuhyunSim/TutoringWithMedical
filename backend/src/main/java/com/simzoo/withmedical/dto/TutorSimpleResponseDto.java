package com.simzoo.withmedical.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.simzoo.withmedical.enums.Subject;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TutorSimpleResponseDto {

    private Long id;
    private java.lang.String imageUrl;
    private java.lang.String nickname;
    private String univName;
    private String location;
    private List<Subject> subjects;

    @QueryProjection
    public TutorSimpleResponseDto(Long id, java.lang.String imageUrl, String nickname,
        String univName, String location,
        List<Subject> subjects) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.univName = univName;
        this.location = location;
        this.subjects = subjects;
    }
}
