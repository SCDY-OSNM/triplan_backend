package scdy.boardservice.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import scdy.boardservice.entity.Board
import scdy.boardservice.enums.BoardCategory

interface BoardRepository : JpaRepository<Board, Long> {

    fun findBoardByCategory(boardCategory: BoardCategory, pageable: Pageable): Page<Board>

    fun findBoardByUserId(userId: Long, pageable: Pageable): Page<Board>
}