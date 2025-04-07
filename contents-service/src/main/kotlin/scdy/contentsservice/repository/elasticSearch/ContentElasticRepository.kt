package scdy.contentsservice.repository.elasticSearch

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import scdy.contentsservice.document.ContentDocument
import scdy.contentsservice.enums.ContentType

interface ContentElasticRepository : ElasticsearchRepository<ContentDocument, Long> {

    fun findByContentName(contentName : String, pageable: Pageable) : Page<ContentDocument>

    fun findByContentType(contentType: ContentType, pageable :Pageable) : Page<ContentDocument>

    fun findByContentExplain(contentExplain : String, pageable: Pageable) : Page<ContentDocument>

    //fun orderByContentGrade(pageable: Pageable) : Page<ContentDocument> // 평점순 정렬

    //fun orderByContentLike(pageable: Pageable) : Page<ContentDocument> // 좋아요순 정렬

    // 컨텐츠 타입 별 조회 + 좋아요 순 정렬
}