package scdy.contentsservice.common.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import scdy.contentsservice.common.exceptions.ElasticSearchIndexException

@Configuration
class ElasticSearchIndexConfig(private val elasticSearchInitializer: ElasticSearchInitializer) {

    @PostConstruct
    fun applySettingOnly(){
        try{
            val indexName = "contents"
            elasticSearchInitializer.initializeIndex(indexName)
        }catch (e : Exception){
            throw ElasticSearchIndexException("ElasticSearch 인덱스 초기화 오류 발생", e)
        }
    }


}