package scdy.contentsservice.service

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery
import co.elastic.clients.elasticsearch.core.*
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import scdy.contentsservice.common.exceptions.ElasticSearchIndexException
import scdy.contentsservice.document.ContentDocument
import scdy.contentsservice.dto.ContentResponseDto
import scdy.contentsservice.entity.Content
import java.io.IOException

@Service
@Slf4j
class ContentSearchService (private val elasticsearchClient: ElasticsearchClient){
    private val logger = org.slf4j.LoggerFactory.getLogger(ContentSearchService::class.java)

    // ES 오타 허용 제목 + 내용 검색
    fun autocomplete(query: String, pageable: Pageable): Page<ContentResponseDto> {
        try {
            // BoolQuery 직접 생성
            val boolQuery = BoolQuery.of { b ->
                b.should { s ->
                    s.multiMatch { mm ->
                        mm.fields("contentName^2", "contentExplain")
                                .query(query)
                                .fuzziness("AUTO")  // 오타 허용
                    }
                }
                b.should { s ->
                    s.multiMatch { mm ->
                        mm.fields("contentName^2", "contentExplain")
                                .query(query)
                                .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.PhrasePrefix)
                    }
                }
            }
            // searchRequest 생성
            val searchRequest = SearchRequest.Builder()
                    .index("contents")
                    .query{it.bool(boolQuery)}
                    .from(pageable.offset.toInt())
                    .size(pageable.pageSize)
                    .build()

            // 검색
            val searchResponse = elasticsearchClient.search(searchRequest, ContentDocument::class.java)

            // 결과 DTO로 변환
            val contents = searchResponse.hits().hits().mapNotNull { it.source()?.let { doc -> ContentResponseDto.from(doc) } }
            val total = searchResponse.hits().total()?.value() ?: 0L

            return PageImpl(contents, pageable, total)

        } catch (e: IOException) {
            logger.error("자동완성 검색 실패:{}", query, e)
            throw ElasticSearchIndexException("자동완성 검색 실패", e)
        }
    }

    // 컨텐츠 인덱스 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveContentIndex(content: Content) {
        try {
            val contentSearch = content.toDocument()

            // IndexRequest 생성
            val request = IndexRequest.of {
                it.index("contents")
                        .id(contentSearch.contentId.toString())
                        .document(contentSearch)
            }

            // Elasticsearch에 데이터 인덱싱 요청
            val response = elasticsearchClient.index(request)

            println("문서가 인덱싱되었습니다. ID: ${response.id()}")

        } catch (e: Exception) {
            throw ElasticSearchIndexException("콘텐츠 인덱싱 실패")
        }
    }

    // 컨텐츠 인덱스 삭제
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun deleteContentIndex(contentId : Long){
        try {
            val request = DeleteRequest.of{ d->
                d.index("contents")
                        .id(contentId.toString())
            }

            val response : DeleteResponse = elasticsearchClient.delete(request)

            if(response.result().name == "Deleted"){
                println("인덱스가 삭제되었습니다. ID : $contentId")
            } else {
                println("인덱스가 존재하지 않거나 이미 삭제된 인덱스입니다 ID : $contentId")
            }
        }catch (e : Exception){
            throw ElasticSearchIndexException("컨텐츠 인덱스 삭제 실패", e)
        }
    }

    // 컨텐츠 인덱스 수정
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateIndex(contentId: Long, updatedFields: Map<String, Any>) {
        try {
            val request = UpdateRequest.of<Map<String, Any>, Map<String, Any>> { u ->
                u.index("contents")
                        .id(contentId.toString())
                        .doc(updatedFields)
            }

            val response: UpdateResponse<Map<String, Any>> = elasticsearchClient.update(request, Map::class.java as Class<Map<String, Any>>)

            if (response.result().name == "Updated") {
                println("인덱스가 업데이트되었습니다. ID: $contentId")
            } else {
                println("컨텐츠 인덱스 업데이트 : ${response.result()}")
            }

        } catch (e: Exception) {
            throw ElasticSearchIndexException("콘텐츠 인덱스 수정 실패", e)
        }
    }

    // 위치 기반 주변 컨텐츠 검색
    fun findNearByContents(lat : Double, lon: Double, distance : String = "10km", pageable: Pageable): Page<ContentResponseDto> {
        return try {
            // BoolQuery 생성
            val boolQuery = BoolQuery.Builder()
                    .filter { f ->
                        f.geoDistance { g ->
                            g.field("contentPoint")
                                    .distance(distance)
                                    .location { loc ->
                                        loc.latlon { l ->
                                            l.lat(lat).lon(lon)
                                        }
                                    }
                        }
                    }
                    .build()

            // SearchRequest 생성 및 실행
            val response = elasticsearchClient.search({ s ->
                s.index("contents")
                        .query { q -> q.bool(boolQuery) }
                        .sort { sort ->
                            sort.geoDistance { g ->
                                g.field("contentPoint")
                                        .location { loc ->
                                            loc.latlon { l ->
                                                l.lat(lat).lon(lon)
                                            }
                                        }
                                        .order(SortOrder.Asc) // 가까운 순서로 정렬
                            }
                        }
                        .from(pageable.offset.toInt())
                        .size(pageable.pageSize)
                        .size(20)
            }, ContentDocument::class.java)

            // 결과 변환
            val contentList = response.hits().hits().mapNotNull { hit -> hit.source()?.let { ContentResponseDto.from(it) } }

            val totalHits = response.hits().total()?.value() ?: 0L

            PageImpl(contentList, pageable, totalHits)

        } catch (e: Exception) {
            throw ElasticSearchIndexException("주변 콘텐츠 검색 실패", e)
        }
    }
}
