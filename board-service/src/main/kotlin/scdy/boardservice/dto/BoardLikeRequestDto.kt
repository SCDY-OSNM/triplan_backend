package scdy.boardservice.dto

data class BoardLikeRequestDto(
    val boardLikeId: Long,
    val userId : Long,
    val boardId : Long
)