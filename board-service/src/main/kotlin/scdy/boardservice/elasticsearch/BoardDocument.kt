package scdy.boardservice.elasticsearch

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import scdy.boardservice.enums.BoardCategory
import java.time.Instant
import java.time.LocalDateTime


@Document(indexName = "board_index")
@Setting(settingPath = "elasticsearch/nori-setting.json")
data class BoardDocument(
    @Id
    var boardId: Long? = null,

    var userId: Long,

    var boardTitle: String,

    var boardContents: String,

    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis])
    var boardCreatedAt: LocalDateTime,

    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis])
    var boardUpdatedAt: LocalDateTime,

    var boardHashtag: String?,

    @Field(type = FieldType.Keyword)
    var boardCategory: BoardCategory
)