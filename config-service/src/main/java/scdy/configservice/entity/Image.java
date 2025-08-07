package scdy.configservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.configservice.enums.ImageType;

@Getter
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    private Long boardId;

    private Long contentsId;

    private Long userId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int imageSize;

    @Builder
    public Image(ImageType imageType,
                 Long userId,
                 Long boardId,
                 Long contentsId,
                 String imageUrl,
                 int imageSize) {
        this.imageType = imageType;
        this.userId = userId;
        this.boardId = boardId;
        this.contentsId = contentsId;
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }


}
