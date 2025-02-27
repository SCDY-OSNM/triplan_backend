package scdy.boardservice.entity

import jakarta.persistence.*

@Entity
class BoardLike (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var boardLikeId : Long? = null,

    @Column(nullable = false)
    var userId : Long,

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var board: Board,
){

}