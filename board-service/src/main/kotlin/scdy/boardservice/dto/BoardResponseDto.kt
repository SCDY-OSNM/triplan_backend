package scdy.boardservice.dto

import scdy.boardservice.elasticsearch.BoardDocument
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
    val boardCategory : BoardCategory,
    // (선택) 게시글 자체의 좋아요 수 필드가 있다면 포함
    val contentLike: Int? = null,
    // (선택) 최근 좋아요 수를 포함하고 싶다면 추가
    val recentLikeCount: Long? = null
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

        fun from(document: BoardDocument): BoardResponseDto {
            return BoardResponseDto(
                boardId = document.boardId,
                userId = document.userId,
                boardTitle = document.boardTitle,
                boardContents = document.boardContents,
                boardCreatedAt = document.boardCreatedAt,
                boardUpdatedAt = document.boardUpdatedAt,
                boardHashtag = document.boardHashtag,
                boardCategory = document.boardCategory
            )
        }

    }


}
