package scdy.boardservice.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import scdy.boardservice.elasticsearch.BoardDocument

interface EsBoardRepository: ElasticsearchRepository<BoardDocument, Long> {

    fun findByBoardTitle(title: String, pageable: Pageable): Page<BoardDocument>

    fun findByBoardContents(contents: String, pageable: Pageable): Page<BoardDocument>

    fun findByBoardHashtag(hashtag: String, pageable: Pageable): Page<BoardDocument>
}