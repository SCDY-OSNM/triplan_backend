package scdy.contentsservice.dto

import scdy.contentsservice.entity.Content
import scdy.contentsservice.enums.ContentType


data class ContentResponseDto(
        val contentId: Long,

        val userId: Long,

        val contentName: String,

        val contentType: ContentType,

        val contentExplain: String,

        val contentGrade: Int,

        val contentAddress: String,

        val contentAmount: Int,

        val contentLatitude: String,

        val contentLongitude: String,

        val contentLike: Int,

        val contentPrice: Int
){
    companion object {
        fun from(content: Content): ContentResponseDto {
            return ContentResponseDto(
                    contentId = content.contentId,
                    userId = content.userId,
                    contentName = content.contentName,
                    contentType = content.contentType,
                    contentExplain = content.contentExplain,
                    contentGrade = content.contentGrade,
                    contentAddress = content.contentAddress,
                    contentAmount = content.contentAmount,
                    contentLatitude = content.contentLatitude,
                    contentLongitude = content.contentLongitude,
                    contentLike = content.contentLike,
                    contentPrice = content.contentPrice
            )
        }
    }
}