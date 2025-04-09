package scdy.contentsservice.dto

import scdy.contentsservice.enums.ContentType


class ContentRequestDto(

        val userId : Long =0,

        val contentName : String ="",

        val contentType : ContentType = ContentType.FREE,

        val contentExplain : String="",

        val contentAddress : String="",

        val contentAmount : Int =0,

        val contentLatitude : String="",

        val contentLongitude : String="",

        val contentPrice : Int =0
)