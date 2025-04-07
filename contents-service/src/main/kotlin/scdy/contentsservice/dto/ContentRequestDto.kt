package scdy.contentsservice.dto

import scdy.contentsservice.enums.ContentType


class ContentRequestDto(

        val userId : Long,

        val contentName : String,

        val contentType : ContentType,

        val contentExplain : String,

        val contentAddress : String,

        val contentAmount : Int,

        val contentLatitude : String,

        val contentLongitude : String,

        val contentPrice : Int
)