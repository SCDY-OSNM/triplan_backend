package scdy.boardservice.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import scdy.boardservice.entity.Board
import scdy.boardservice.enums.BoardCategory

interface BoardCustomRepository {
    fun findBoardByBoardCategory(boardCategory: BoardCategory, pageable: Pageable): Page<Board>

    fun findBoardByUserId(userId: Long, pageable: Pageable): Page<Board>

    fun findByIdOrElseThrow(boardId: Long): Board
}