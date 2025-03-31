package scdy.boardservice.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import scdy.boardservice.elasticsearch.BoardDocument
import scdy.boardservice.entity.Board

interface EsBoardRepository: ElasticsearchRepository<BoardDocument, Long> {

    fun findByBoardTitle(title: String, pageable: Pageable): Page<Board>

    fun findByBoardContents(contents: String, pageable: Pageable): Page<Board>

    fun findByBoardHashtag(hashtag: String, pageable: Pageable): Page<Board>

}