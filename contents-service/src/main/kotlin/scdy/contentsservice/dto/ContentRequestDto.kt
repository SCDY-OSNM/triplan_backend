package scdy.contentsservice.dto

import scdy.contentsservice.entity.Content
import scdy.contentsservice.enums.ContentType


class ContentRequestDto(

        var userId : Long,

        var contentName : String,

        var contentType : ContentType,

        var contentExplain : String,

        var contentAddress : String,

        var contentAmount : Int,

        var contentLatitude : String,

        var contentLongitude : String,

        var contentPrice : Int
)