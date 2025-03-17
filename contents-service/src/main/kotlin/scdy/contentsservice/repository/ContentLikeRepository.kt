package scdy.contentsservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import scdy.contentsservice.entity.Content
import scdy.contentsservice.entity.ContentLike
import scdy.contentsservice.exception.ContentLikeNotFoundException
import java.util.*

interface ContentLikeRepository :JpaRepository<ContentLike, Long> {
    /*fun findByIdOrElseThrow(contentLikeId : Long) : ContentLike{
        return findById(contentLikeId).orElseThrow{ContentLikeNotFoundException("좋아요를 찾을 수 없습니다.")}
    }*/

    fun findByContentAndUserId(content: Content, userId: Long) : Optional<ContentLike>

    fun findByContentId(contentId : Long) : List<ContentLike>

    fun findByUserId(userId : Long): List<ContentLike>
}