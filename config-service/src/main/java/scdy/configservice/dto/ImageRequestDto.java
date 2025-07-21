package scdy.configservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.configservice.enums.ImageType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDto {

    private ImageType imageType;

    private Long boardId;

    private Long contentsId;

    private Long userId;
}
