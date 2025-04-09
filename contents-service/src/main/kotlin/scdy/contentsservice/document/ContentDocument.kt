package scdy.contentsservice.document

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import scdy.contentsservice.enums.ContentType

@Document(indexName = "content")
@Mapping(mappingPath = "/elastic-mapping.json")
@Setting(settingPath = "/elastic-setting.json") // 여기 노리 세팅 들어가야됨.
@JsonIgnoreProperties(ignoreUnknown = true)
class ContentDocument @JsonCreator constructor(
        @Id
        @JsonProperty("contentId")
        val contentId: Long? = null,

        @JsonProperty("userId")
        var userId: Long,

        @JsonProperty("contentName")
        var contentName: String,

        @JsonProperty("contentType")
        var contentType: ContentType,

        @JsonProperty("contentExplain")
        var contentExplain: String,

        @JsonProperty("contentGrade")
        var contentGrade: Int,

        @JsonProperty("contentAddress")
        var contentAddress: String,

        @JsonProperty("contentAmount")
        var contentAmount: Int,

        @JsonProperty("contentLike")
        var contentLike: Int,

        @JsonProperty("contentPoint")
        @GeoPointField
        var contentPoint: GeoPoint,

        @JsonProperty("contentLatitude")
        var contentLatitude: String,

        @JsonProperty("contentLongitude")
        var contentLongitude: String,

        @JsonProperty("contentPrice")
        var contentPrice: Int
){

}