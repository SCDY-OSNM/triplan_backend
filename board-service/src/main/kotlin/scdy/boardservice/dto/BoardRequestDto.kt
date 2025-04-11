package scdy.boardservice.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import scdy.boardservice.enums.BoardCategory
import java.time.LocalDateTime

data class BoardRequestDto @JsonCreator constructor(
    @JsonProperty("boardId") val boardId: Long? = null,
    @JsonProperty("userId") val userId: Long? = null,
    @JsonProperty("boardTitle") val boardTitle: String,
    @JsonProperty("boardContents") val boardContents: String,
    @JsonProperty("boardHashtag") val boardHashtag: String,
    @JsonProperty("boardCategory") val boardCategory: BoardCategory
)