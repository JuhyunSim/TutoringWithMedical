package com.simzoo.withmedical.dto;

import com.simzoo.withmedical.entity.MemberEntity;
import com.simzoo.withmedical.entity.ReviewEntity;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {

    private Long tutorProfileId;
    private Double rating;
    @Size(min = 1, max = 300)
    private String reviewText;

    public ReviewEntity toEntity(MemberEntity tutor, MemberEntity writer) {
        return ReviewEntity.builder()
            .tutorProfile(tutor.getTutorProfile())
            .writer(writer)
            .content(reviewText)
            .rating(rating)
            .build();
    }

}
