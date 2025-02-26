package scdy.contentsservice.dto

import scdy.contentsservice.entity.Content

class ContentLikeRequestDto (

        var userId : Long,

        var content : Content
){
}