package scdy.boardservice.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Getter
@NoArgsConstructor
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var commentId : Long,

    @Column(nullable = false)
    var userId : Long,

    @Column(nullable = false)
    var contents : String,

    @Column(nullable = false)
    var createdAt : LocalDateTime,

    @Column(nullable = false)
    var updatedAt : LocalDateTime,

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var board: Board
) {
}