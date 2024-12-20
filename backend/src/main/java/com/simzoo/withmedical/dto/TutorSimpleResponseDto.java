package com.simzoo.withmedical.dto;

import com.querydsl.core.annotations.QueryProjection;
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
    private List<String> subjects;

    @QueryProjection
    public TutorSimpleResponseDto(Long id, java.lang.String imageUrl, String nickname,
        String univName, String location,
        List<String> subjects) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.univName = univName;
        this.location = location;
        this.subjects = subjects;
    }
}
