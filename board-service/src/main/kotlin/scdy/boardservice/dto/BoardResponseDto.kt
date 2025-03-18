package scdy.boardservice.dto

import scdy.boardservice.entity.Board
import scdy.boardservice.enums.BoardCategory
import java.time.LocalDateTime

data class BoardResponseDto(
    val boardId : Long?,
    val userId : Long,
    val boardTitle : String,
    val boardContents : String,
    val boardCreatedAt : LocalDateTime,
    val boardUpdatedAt : LocalDateTime,
    val boardHashtag : String?,
    val boardCategory : BoardCategory
){
    companion object {
        fun from(board: Board): BoardResponseDto {
            return BoardResponseDto(
                boardId = board.boardId,
                userId = board.userId,
                boardTitle = board.boardTitle,
                boardContents = board.boardContents,
                boardCreatedAt = board.boardCreatedAt,
                boardUpdatedAt = board.boardUpdatedAt,
                boardHashtag = board.boardHashtag,
                boardCategory = board.boardCategory
            )
        }
    }
}
