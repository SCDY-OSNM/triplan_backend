package scdy.boardservice.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import scdy.boardservice.elasticsearch.BoardLikeDocument
import java.time.LocalDateTime

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "boardId"])])
@EntityListeners(AuditingEntityListener::class)
class BoardLike (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var boardLikeId : Long? = null,

    @Column(nullable = false)
    var userId : Long,

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var board: Board,

    @CreatedDate
    var boardLikeCreatedAt: LocalDateTime,

    ){
    fun toDocument(): BoardLikeDocument{
        return BoardLikeDocument(
            boardLikeId = this.boardLikeId,
            boardId = this.board.boardId,
            userId = this.userId,
            boardLikeCreatedAt = this.boardLikeCreatedAt,
        )
    }
}