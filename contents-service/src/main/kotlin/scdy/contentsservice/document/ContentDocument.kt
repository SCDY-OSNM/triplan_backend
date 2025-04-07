package scdy.contentsservice.document

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import scdy.contentsservice.enums.ContentType

@Document(indexName = "content")
@Mapping(mappingPath = "resources/elastic-setting.json")
@Setting(settingPath = "elastic-setting.json") // 여기 노리 세팅 들어가야됨.
class ContentDocument(

        @Id
        val contentId : Long? = null,

        @Field(name = "userId", type = FieldType.Long)
        var userId : Long,

        @Field(name = "contentName" , type = FieldType.Text, analyzer = "nori_analyzer", searchAnalyzer = "nori_search_analyzer")
        var contentName : String,

        @Field(name = "contentType", type = FieldType.Keyword)
        var contentType : ContentType,

        @Field(name = "contentExplain", type = FieldType.Text, analyzer = "nori_analyzer", searchAnalyzer = "nori_search_analyzer")
        var contentExplain : String,

        @Field(name = "contentGrade", type = FieldType.Integer)
        var contentGrade : Int,

        var contentAddress : String,

        var contentAmount : Int,

        @Field(name = "contentLike", type = FieldType.Integer)
        var contentLike : Int,

        @GeoPointField
        var contentPoint : GeoPoint,

        var contentLatitude : String,

        var contentLongitude : String,

        var contentPrice : Int
){

}