package scdy.boardservice.dto

data class BoardUpdateRequestDto(
    val boardTitle: String,
    val boardContents: String,
    val boardHashtag: String
)
