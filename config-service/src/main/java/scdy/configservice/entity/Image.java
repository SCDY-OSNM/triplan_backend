package scdy.configservice.entity;

import jakarta.persistence.*;
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
    private ImageType imageType;

    private Long boardId;

    private Long contentsId;

    private Long userId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int imageSize;
}
