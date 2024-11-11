package com.simzoo.withmedical.entity;

import com.simzoo.withmedical.dto.ReviewResponseDto;
import com.simzoo.withmedical.dto.UpdateReviewRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

@Entity(name = "review")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private MemberEntity writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutorProfileId")
    private TutorProfileEntity tutorProfile;
    private String content;
    private Double rating;

    public ReviewResponseDto toResponseDto() {
        return ReviewResponseDto.builder()
            .writerNickname(writer.getNickname())
            .rating(rating)
            .reviewText(content)
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }

    public void update(UpdateReviewRequestDto reviewRequestDto) {
        this.rating = reviewRequestDto.getRating();
        this.content = reviewRequestDto.getReviewText();
    }
}
