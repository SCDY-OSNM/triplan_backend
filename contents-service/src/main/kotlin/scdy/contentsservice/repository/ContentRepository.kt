package scdy.contentsservice.repository

import jakarta.ws.rs.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import scdy.contentsservice.entity.Content
import java.util.*

interface ContentRepository : JpaRepository<Content, Long> {
    fun findByIdOrElseThrow(contentId: Long) :Content{
       return findById(contentId).orElseThrow{NotFoundException("컨텐츠를 찾을 수 없습니다.")}
    }
}