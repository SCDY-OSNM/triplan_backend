package scdy.boardservice.service

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.DeleteRequest
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.core.IndexResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import scdy.boardservice.elasticsearch.BoardDocument
import scdy.boardservice.entity.Board
import scdy.boardservice.entity.BoardLike
import scdy.boardservice.exception.ElasticsearchIndexException
import scdy.boardservice.repository.EsBoardRepository
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import scdy.boardservice.repository.BoardLikeRepository


@Service
class SearchService(
    private val elasticsearchClient: ElasticsearchClient,
    private val objectMapper: ObjectMapper, // ObjectMapper 빈 주입
    private val boardLikeRepository: BoardLikeRepository,
    private val esBoardRepository: EsBoardRepository

) {

    //인덱스에 게시물 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveBoardIndex(board: Board) {

        try {
            val boardDocument = board.toDocument()

            val json = objectMapper.writeValueAsString(boardDocument)

            val request = IndexRequest.Builder<BoardDocument>()
                .index("board_index")
                .id(boardDocument.boardId.toString())
                .withJson(ByteArrayInputStream(json.toByteArray(Charsets.UTF_8)))
                .build()

            val response: IndexResponse = elasticsearchClient.index(request)

            println("문서가 인덱싱되었습니다. 문서 ID: ${response.id()}")

        } catch (e: Exception) {
            throw ElasticsearchIndexException(e.toString())
        }
    }

    //인덱스에서 게시글 업데이트
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateBoardIndex(board: Board) {
        try {
            val boardDocument = board.toDocument()

            val json = objectMapper.writeValueAsString(boardDocument)

            val request = IndexRequest.Builder<BoardDocument>()
                .index("board_index")
                .id(boardDocument.boardId.toString())
                .withJson(ByteArrayInputStream(json.toByteArray(Charsets.UTF_8)))
                .build()

            val response: IndexResponse = elasticsearchClient.index(request)

            println("문서가 업데이트되었습니다. 문서 ID: ${response.id()}")

        } catch (e: Exception) {
            throw ElasticsearchIndexException("게시글 인덱스 수정 실패",)
        }
    }

    //인덱스에서 게시물 삭제
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun deleteBoardIndex(boardId : Long) {

        try{
            val deleteRequest = DeleteRequest.of { i ->
                i.index("board_index")
                .id(boardId.toString())
            }

            val response = elasticsearchClient.delete(deleteRequest)

            if (response.result().name == "Deleted") {
                println("문서가 성공적으로 삭제되었습니다. 문서 ID: $boardId");
            } else {
                println("문서가 존재하지 않거나 이미 삭제되었습니다. 문서 ID: $boardId");
            }
        }catch(e: Exception){
            throw ElasticsearchIndexException("게시글 인덱스 삭제 실")
        }
    }


    //인덱스에 좋아요 문서 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveBoardLikeIndex(boardLike: BoardLike) {

        try {
            val boardLikeDocument = boardLike.toDocument()

            val json = objectMapper.writeValueAsString(boardLikeDocument)

            val request = IndexRequest.Builder<BoardDocument>()
                .index("board_like_index")
                .id(boardLikeDocument.boardLikeId.toString())
                .withJson(ByteArrayInputStream(json.toByteArray(Charsets.UTF_8)))
                .build()

            val response: IndexResponse = elasticsearchClient.index(request)

            println("문서가 인덱싱되었습니다. 문서 ID: ${response.id()}")

        } catch (e: Exception) {
            throw ElasticsearchIndexException(e.toString())
        }
    }

    //실시간 최근 1시간 좋아요 상위 게시글
    fun getPopularInLastHour(): List<BoardDocument> {
        val oneHourAgo = LocalDateTime.now().minusHours(1)

        // 최근 1시간 이내의 좋아요 리스트
        val recentLikes = boardLikeRepository.findByBoardLikeCreatedAtAfter(oneHourAgo)

        // boardId 별 좋아요 수 집계
        val boardLikeCountMap = recentLikes.groupingBy { it.board.boardId }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(10)
            .map { it.first }

        // 엘라스틱서치에서 boardId로 board document 조회
        return esBoardRepository.findAllById(boardLikeCountMap).toList()
    }



    //자동완성

}
