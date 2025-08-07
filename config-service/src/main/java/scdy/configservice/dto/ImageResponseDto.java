package scdy.configservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.configservice.entity.Image;
import scdy.configservice.enums.ImageType;

@Getter
@NoArgsConstructor
public class ImageResponseDto {

    private Long imageId;

    private ImageType imageType;

    private Long boardId;

    private Long contentsId;

    private Long userId;

    private String imageUrl;

    private int imageSize;


    public ImageResponseDto(Long imageId,
                            ImageType imageType,
                            Long boardId,
                            Long contentsId,
                            Long userId,
                            String imageUrl,
                            int imageSize) {
        this.imageId = imageId;
        this.imageType = imageType;
        this.boardId = boardId;
        this.contentsId = contentsId;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }

    public static ImageResponseDto from(Image image) {
        return new ImageResponseDto(
                image.getImageId(),
                image.getImageType(),
                image.getBoardId(),
                image.getContentsId(),
                image.getUserId(),
                image.getImageUrl(),
                image.getImageSize()
        );
    }
}
