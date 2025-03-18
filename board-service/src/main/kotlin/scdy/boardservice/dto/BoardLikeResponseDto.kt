package scdy.boardservice.dto

import scdy.boardservice.entity.BoardLike

data class BoardLikeResponseDto (
    val boardLikeId: Long?,
    val userId: Long,
    val boardId : Long?
){
    companion object{
        fun from(boardLike: BoardLike): BoardLikeResponseDto {
            return BoardLikeResponseDto(
                boardLikeId = boardLike.boardLikeId,
                userId = boardLike.userId,
                boardId = boardLike.board.boardId
            )
        }
    }
}