package scdy.contentsservice.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import scdy.contentsservice.entity.Content
import scdy.contentsservice.entity.ContentLike
import java.util.*

interface ContentLikeRepository :JpaRepository<ContentLike, Long> {
    /*fun findByIdOrElseThrow(contentLikeId : Long) : ContentLike{
        return findById(contentLikeId).orElseThrow{ContentLikeNotFoundException("좋아요를 찾을 수 없습니다.")}
    }*/

    fun findByContentAndUserId(content: Content, userId: Long) : Optional<ContentLike>

    fun findByContent(content : Content) : List<ContentLike>

    fun findByUserId(userId : Long): List<ContentLike>
}