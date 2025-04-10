package scdy.reservationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentResponseDto {
    @JsonProperty("contentId")
    private Long contentId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("contentName")
    private String contentName;

    private String contentType;

    private String contentExplain;

    private int contentGrade;

    private String contentAddress;

    private int contentAmount;

    private int contentLike;

    private String contentLatitude;

    private String contentLongitude;

    private int contentPrice;
}
