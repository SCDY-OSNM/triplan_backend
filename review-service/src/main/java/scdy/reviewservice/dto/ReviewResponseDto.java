package scdy.reviewservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.reviewservice.entity.Review;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;

    private Long userId;

    private Long contentsId;

    private String content;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private Integer star;

    @Builder
    public ReviewResponseDto(Long reviewId, Long userId, Long contentsId, String content, LocalDateTime createAt, LocalDateTime updateAt, Integer star) {

        this.reviewId = reviewId;
        this.userId = userId;
        this.contentsId = contentsId;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.star = star;
    }


    public static ReviewResponseDto from(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUserId())
                .contentsId(review.getContentsId())
                .content(review.getContent())
                .createAt(review.getCreateAt())
                .updateAt(review.getUpdateAt())
                .star(review.getStar())
                .build();
    }
}
