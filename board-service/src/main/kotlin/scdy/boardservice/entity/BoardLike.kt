package scdy.boardservice.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Getter
@NoArgsConstructor
class BoardLike (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var boardLikeId : Long,

    @Column(nullable = false)
    var userId : Long,

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var board: Board,
){

}