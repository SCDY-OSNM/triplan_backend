package scdy.boardservice.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import scdy.boardservice.enums.BoardCategory
import java.time.LocalDateTime

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener::class)
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var boardId : Long,

    @Column(nullable = false)
    private var userId : Long,

    @Column(nullable = false)
    private var boardTitle : String,

    @Column(nullable = false)
    private var boardContents : String,

    @Column(nullable = false)
    @CreatedDate
    private var boardCreatedAt : LocalDateTime,

    @Column(nullable = false)
    @LastModifiedDate
    private var boardUpdatedAt : LocalDateTime,

    @Column(nullable = false)
    private var boardHashtag : String,

    @Column(nullable = false)
    private var boardCategory :BoardCategory,
) {

}