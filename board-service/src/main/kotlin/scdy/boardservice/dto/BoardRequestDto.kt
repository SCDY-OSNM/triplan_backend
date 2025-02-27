package scdy.boardservice.dto

import scdy.boardservice.enums.BoardCategory
import java.time.LocalDateTime

data class BoardRequestDto (
    val boardId : Long,
    val userId : Long,
    val boardTitle : String,
    val boardContents : String,
    val boardHashtag : String,
    val boardCategory : BoardCategory
)