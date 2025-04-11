package scdy.boardservice.elasticsearch

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import scdy.boardservice.entity.Board
import scdy.boardservice.enums.BoardCategory
import java.time.Instant
import java.time.LocalDateTime


@Document(indexName = "board_index")
@Setting(settingPath = "elasticsearch/nori-setting.json")
@Mapping(mappingPath = "elasticsearch/board-mapping.json")
data class BoardDocument(
    @Id
    var boardId: Long? = null,

    var userId: Long,

    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    var boardTitle: String,

    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    var boardContents: String,

    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis, DateFormat.strict_date_time])
    var boardCreatedAt: LocalDateTime,

    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis, DateFormat.strict_date_time])
    var boardUpdatedAt: LocalDateTime,

    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    var boardHashtag: String?,

    @Field(type = FieldType.Keyword)
    var boardCategory: BoardCategory,

    @Field(type = FieldType.Integer)
    var boardLike: Int = 0
){
    companion object{
        fun from(board: Board): BoardDocument{
            return BoardDocument(
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