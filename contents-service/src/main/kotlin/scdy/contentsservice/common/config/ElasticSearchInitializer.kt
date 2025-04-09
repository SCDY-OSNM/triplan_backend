package scdy.contentsservice.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.type.TypeReference
import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.ExistsRequest
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest
import co.elastic.clients.elasticsearch.indices.IndexSettings
import co.elastic.clients.json.JsonData
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import scdy.contentsservice.common.exceptions.ElasticSearchIndexException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.lang.IllegalArgumentException

@Component
class ElasticSearchInitializer(private val client: ElasticsearchClient,
                               private val jacksonConfig: JacksonConfig,
                               private val objectMapper: ObjectMapper){



    // 인덱스 존재 여부 확인
    private fun indexExist(indexName : String) : Boolean{
        return client.indices().exists { it.index(indexName) }.value()
    }

    // Json파일 Map으로 변환
    private fun loadJsonFileAsMap(path: String): Map<String, Any> {
        try {
            val inputStream = this::class.java.classLoader.getResourceAsStream(path)
                    ?: throw IllegalArgumentException("파일을 찾을 수 없습니다: $path")

            return jacksonConfig.objectMapper().readValue(
                    inputStream,
                    object : TypeReference<Map<String, Any>>() {}
            )
        } catch (e: IOException) {
            throw ElasticSearchIndexException("JSON 파일 읽기 중 오류 발생: ${e.message}", e)
        } catch (e: Exception) {
            throw ElasticSearchIndexException("JSON 파일 처리 중 오류 발생: ${e.message}", e)
        }
    }

    // Json파일 JsonNode로 변환
    private fun readJsonFromFile(path: String): JsonNode {
        return try {
            val inputStream = javaClass.classLoader.getResourceAsStream(path)
                    ?: throw IllegalArgumentException("파일을 찾을 수 없습니다: $path")

            inputStream.use {
                objectMapper.readTree(it)
            }
        } catch (e: IOException) {
            throw ElasticSearchIndexException("Json파일 읽기 중 오류 발생: ${e.message}\", e")
        } catch (e: Exception) {
            throw ElasticSearchIndexException("Json파일 처리 중 오류 발생: ${e.message}\", e")
        }
    }

    public fun initializeIndex(indexName: String){
        if(!indexExist(indexName)){
            try{
            // setting.json 로드
            val settingsMap: Map<String, Any> = try {
                loadJsonFileAsMap("elastic-setting.json")
            } catch (e: IOException) {
                throw ElasticSearchIndexException("settings.json 파일 로드 중 오류 발생", e)
            } catch (e: Exception) {
                throw ElasticSearchIndexException("settings.json 처리 중 일반적인 오류 발생", e)
            }

            // mapping.json 로드
            val mappingsNode : JsonNode = try{
                readJsonFromFile("elastic-mapping.json")
            } catch (e: Exception){
                throw ElasticSearchIndexException("mapping.json 파일 로드 중 오류 발생", e)
            }

            // mapping 노드 확인
            val mappingsData = mappingsNode.path("mappings")
            if(mappingsData.isMissingNode){
                throw ElasticSearchIndexException("mapping.json에 mappins필드가 포함되지 않음.")
            }

            // mappins데이터를 json으로 변환
            val mappingsJsonString = objectMapper.writeValueAsString(mappingsData)
            val mappingsStream = ByteArrayInputStream(mappingsJsonString.toByteArray(Charsets.UTF_8))

            // jsonData로 설정 적용
            val settings = IndexSettings.of{ builder->
                settingsMap.forEach{ (key, value) ->
                    builder.otherSettings(key, JsonData.of(value))
                }
                builder
            }

            // 인덱스 생성 요청
            val request = CreateIndexRequest.of{ builder ->
                builder.index(indexName)
                        .settings(settings)
                        .mappings{it.withJson(mappingsStream)}
            }

            client.indices().create(request)
            println("인덱스 생성 완료 : $indexName")
            } catch (e: IOException) {
                throw ElasticSearchIndexException("Elasticsearch 파일 처리 중 입출력 오류 발생", e)
            } catch (e: Exception) {
                throw ElasticSearchIndexException("Elasticsearch 인덱스 생성 중 예외 발생", e)
            }
        }
    }
}