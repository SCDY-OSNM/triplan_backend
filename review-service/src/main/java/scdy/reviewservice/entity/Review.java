package scdy.reviewservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private Long userId;

    private Long contentsId;

    private String content;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    private int star;

    @Builder

    public Review(Long userId, Long contentsId, String content, LocalDateTime createAt, LocalDateTime updateAt, int star) {
        this.userId = userId;
        this.contentsId = contentsId;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.star = star;
    }
}
