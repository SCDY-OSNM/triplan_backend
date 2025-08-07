package scdy.boardservice.repository

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import scdy.boardservice.elasticsearch.BoardLikeDocument

interface EsBoardLikeRepository: ElasticsearchRepository<BoardLikeDocument, Long> {


}