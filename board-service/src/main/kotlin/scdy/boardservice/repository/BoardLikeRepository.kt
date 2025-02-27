package scdy.boardservice.repository

import org.bouncycastle.util.Integers
import org.springframework.data.jpa.repository.JpaRepository
import scdy.boardservice.entity.BoardLike
import java.util.*

interface BoardLikeRepository: JpaRepository<BoardLike, Long> {

    fun findByUserId(userId: Long): BoardLike

    fun findByUserIdAndBoardId(userId: Long, boardId: Long): Optional<BoardLike>

    fun findNumberByBoardId(boardid: Long) : Int
}