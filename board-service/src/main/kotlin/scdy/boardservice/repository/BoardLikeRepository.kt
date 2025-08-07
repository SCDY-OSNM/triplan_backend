package scdy.boardservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import scdy.boardservice.entity.BoardLike
import java.time.LocalDateTime
import java.util.*

interface BoardLikeRepository: JpaRepository<BoardLike, Long>, BoardLikeCustomRepository {

    fun countByBoard_BoardId(boardId: Long): Long

    fun findByBoardLikeCreatedAtAfter(time: LocalDateTime): List<BoardLike>
}