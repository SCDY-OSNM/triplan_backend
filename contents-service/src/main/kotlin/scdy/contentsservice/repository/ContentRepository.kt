package scdy.contentsservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import scdy.contentsservice.entity.Content
import scdy.contentsservice.enums.ContentType
import scdy.contentsservice.exception.ContentNotFoundException

interface ContentRepository : JpaRepository<Content, Long> {

    fun findByContentType(contentType : ContentType): List<Content>

    /*fun findByIdOrElseThrow(contentId: Long) :Content{
       return findById(contentId).orElseThrow{ContentNotFoundException("컨텐츠를 찾을 수 없습니다.")}
    }*/
}