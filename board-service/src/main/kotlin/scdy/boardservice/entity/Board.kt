package scdy.boardservice.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import scdy.boardservice.enums.BoardCategory
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var boardId: Long? = null,

    @Column(nullable = false)
    var userId: Long,

    @Column(nullable = false)
    var boardTitle: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var boardContents: String,

    @CreatedDate
    var boardCreatedAt: LocalDateTime,

    @LastModifiedDate
    var boardUpdatedAt: LocalDateTime,

    var boardHashtag: String?,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var boardCategory: BoardCategory
) {

    fun updateBoard(boardTitle: String, boardContents: String, boardHashtag: String?) {
        this.boardTitle = boardTitle
        this.boardContents = boardContents
        this.boardHashtag = boardHashtag
    }

}