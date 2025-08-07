package scdy.boardservice.service

import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.stereotype.Repository

@Repository
class LiveSearchRepository(
    private val elasticsearchOperations: ElasticsearchOperations,
){

}