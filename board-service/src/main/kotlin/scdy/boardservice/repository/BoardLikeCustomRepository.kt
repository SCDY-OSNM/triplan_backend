package scdy.boardservice.repository

import scdy.boardservice.entity.BoardLike
import java.util.*

interface BoardLikeCustomRepository {

    fun findByUserId(userId: Long): BoardLike?

    fun findByUserIdAndId(userId: Long, boardId: Long): Optional<BoardLike>

    fun findNumberByBoardId(boardId: Long) : Int
}