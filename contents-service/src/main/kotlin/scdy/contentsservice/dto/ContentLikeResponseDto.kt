package scdy.contentsservice.dto

import scdy.contentsservice.entity.Content
import scdy.contentsservice.entity.ContentLike


class ContentLikeResponseDto (

        var userId : Long,

        var content : Content
){
    companion object{
        fun from(contentLike: ContentLike) :ContentLikeResponseDto{
            return ContentLikeResponseDto(
                    userId = contentLike.userId,
                    content = contentLike.content
            )
        }
    }

}