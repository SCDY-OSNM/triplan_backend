package scdy.contentsservice.entity

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import scdy.contentsservice.document.ContentDocument
import scdy.contentsservice.enums.ContentType

@Entity
@Getter
@NoArgsConstructor
@Table(name = "content")
class Content(
    @Id
    @Column(name= "contentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val contentId : Long? = null,

    @Column(nullable = false)
    var userId : Long,

    @Column(nullable = false)
    var contentName : String,

    @Column(nullable = false)
    var contentType : ContentType,

    @Column(nullable = false)
    var contentExplain : String,

    @Column(nullable = false)
    var contentGrade : Int,

    @Column(nullable = false)
    var contentAddress : String,

    var contentAmount : Int,

    @Column(nullable = false)
    var contentLike : Int,

    @Column(nullable = false)
    var contentLatitude : String,

    @Column(nullable = false)
    var contentLongitude : String,

    var contentPrice : Int
    ){
    fun updateContent(contentName : String, contentType : ContentType, contentExplain: String, contentAmount: Int, contentPrice : Int){
        this.contentName = contentName
        this.contentType = contentType
        this.contentExplain =contentExplain
        this.contentAmount = contentAmount
        this.contentPrice = contentPrice
    }

    fun contentLikeUp(){
        this.contentLike += 1
    }

    fun contentLikeDown(){
        this.contentLike -= 1
    }

    fun updateLocation(contentAddress: String, contentLatitute : String, contentLongitute : String){
        this.contentAddress = contentAddress
        this.contentLatitude = contentLatitute
        this.contentLongitude = contentLongitute
    }

    fun toDocument() : ContentDocument {
        val lat = this.contentLatitude.toDoubleOrNull()
        val lon = this.contentLongitude.toDoubleOrNull()

        val geoPoint = if(lat != null && lon != null) GeoPoint(lat, lon) else GeoPoint(0.0,0.0)
        return ContentDocument(
                contentId = this.contentId,
                userId = this.userId,
                contentName = this.contentName,
                contentType = this.contentType,
                contentExplain = this.contentExplain,
                contentGrade = this.contentGrade,
                contentAddress = this.contentAddress,
                contentAmount = this.contentAmount,
                contentLike = this.contentLike,
                contentPoint = geoPoint,
                contentLatitude = this.contentLatitude,
                contentLongitude = this.contentLongitude,
                contentPrice = this.contentPrice
        )

    }
}