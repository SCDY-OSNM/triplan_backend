package scdy.reservationservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentsResponseDto {
    private Long contentsId;

    private Long userId;

    private String contentsName;

    private String contentsType;

    private String contentsExplain;

    private int contentsGrade;

    private String contentsAddress;

    private int contentsAmount;

    private int contentsLike;

    private String contentsLatitude;

    private String contentsLongitude;

    private int contentsPrice;
}
