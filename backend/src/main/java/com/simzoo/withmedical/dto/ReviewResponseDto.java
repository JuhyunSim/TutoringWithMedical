package com.simzoo.withmedical.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class ReviewResponseDto {

    private String tutorNickname;
    private String writerNickname;
    private Double rating;
    private String reviewText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public ReviewResponseDto(String tutorNickname, String writerNickname, Double rating, String reviewText, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.tutorNickname = tutorNickname;
        this.writerNickname = writerNickname;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
