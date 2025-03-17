package scdy.boardservice.dto

data class CommentRequestDto (
    val contents : String,
    val boardId : Long
)