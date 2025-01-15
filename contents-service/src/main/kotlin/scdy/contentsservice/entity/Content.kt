package scdy.contentsservice.entity

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.contentsservice.enums.ContentType

@Entity
@Getter
@NoArgsConstructor
@Table(name = "content")
class Content(
    @Id
    @Column(name= "contentId")
    var contentId : Long,

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
    fun updateContent(contentName : String, contentType : ContentType, contentExplain: String, contentAddress: String, contentAmount: Int){
        this.contentName = contentName
        this.contentType = contentType
        this.contentExplain =contentExplain
        this.contentAddress = contentAddress
        this.contentAmount = contentAmount
    }

    fun updateContentLike(contentLike : Int){
        this.contentLike = contentLike
    }

    fun updateLocation(contentLatitute : String, contentLongitute : String){
        this.contentLatitude = contentLatitute
        this.contentLongitude = contentLongitute
    }
    companion object{
        fun of(contentId: Long, userId: Long, contentName : String, contentType: ContentType,
               contentGrade: Int, contentExplain: String, contentAddress: String, contentAmount: Int, contentLike: Int, contentLatitute: String, contentLongitude: String, contentPrice: Int ): Content{
            return Content(
                contentId = contentId,
                userId = userId,
                contentName = contentName,
                contentType = contentType,
                contentGrade = contentGrade,
                contentExplain = contentExplain,
                contentAddress = contentAddress,
                contentLike = contentLike,
                contentLatitude = contentLatitute,
                contentLongitude = contentLongitude,
                contentAmount = contentAmount,
                contentPrice = contentPrice,
            )
        }
    }
}