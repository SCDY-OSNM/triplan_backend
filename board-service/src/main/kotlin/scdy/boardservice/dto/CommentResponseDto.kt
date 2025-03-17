package scdy.boardservice.dto

import scdy.boardservice.entity.Board
import scdy.boardservice.entity.Comment
import java.time.LocalDateTime

data class CommentResponseDto(
    val commentId : Long,
    val userId : Long,
    val contents: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val boardId: Long?
){
    companion object{
        fun from(comment: Comment): CommentResponseDto {
            return CommentResponseDto(
                commentId = comment.commentId,
                userId = comment.userId,
                contents = comment.contents,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
                boardId = comment.board.boardId
            )
        }
    }
}
