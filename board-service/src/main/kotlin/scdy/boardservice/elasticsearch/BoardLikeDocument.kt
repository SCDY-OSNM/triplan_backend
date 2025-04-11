package scdy.boardservice.elasticsearch

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDateTime

@Document(indexName = "board_like_index")
data class BoardLikeDocument(

    @Id
    var boardLikeId: Long? = null,

    @Field(type = FieldType.Long)
    var boardId: Long? = null,

    @Field(type = FieldType.Long)
    var userId: Long,

    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis, DateFormat.strict_date_time])
    var boardLikeCreatedAt: LocalDateTime = LocalDateTime.now()
)