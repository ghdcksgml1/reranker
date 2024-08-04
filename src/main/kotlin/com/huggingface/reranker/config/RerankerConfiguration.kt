package com.huggingface.reranker.config

import ai.djl.repository.zoo.Criteria
import ai.djl.repository.zoo.ZooModel
import ai.djl.training.util.ProgressBar
import ai.djl.util.StringPair
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(RerankerProperties::class)
class RerankerConfiguration {

    @Bean
    fun rerankerModel(rerankerProperties: RerankerProperties): ZooModel<StringPair, FloatArray> {
        val criteria = Criteria.builder()
            .setTypes(StringPair::class.java, FloatArray::class.java)
            .optModelUrls(rerankerProperties.modelUrls)
            .optEngine(rerankerProperties.engine)
            .optArgument("reranking", "true")
            .optProgress(ProgressBar())
            .build()

        return criteria.loadModel()
    }

    @Bean
    fun restClientBuilder(): RestClient.Builder {
        return RestClient.builder()
    }
}