package scdy.reviewservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

    private Long userId;

    private Long contentsId;

    private String content;

    private Integer star;


    @Builder
    public ReviewRequestDto(Long userId, Long contentsId, String content, Integer star){
        this.userId = userId;
        this.contentsId = contentsId;
        this.content = content;
        this.star = star;
    }
}
