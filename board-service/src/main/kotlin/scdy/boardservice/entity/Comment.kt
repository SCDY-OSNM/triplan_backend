package scdy.boardservice.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener::class)
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var commentId : Long,

    @Column(nullable = false)
    var userId : Long,

    @Column(nullable = false)
    var contents : String,

    @Column(nullable = false)
    @CreatedDate
    var createdAt : LocalDateTime,

    @Column(nullable = false)
    @LastModifiedDate
    var updatedAt : LocalDateTime,

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var board: Board
) {
}