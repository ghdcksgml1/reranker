package com.huggingface.reranker.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("reranker")
data class RerankerProperties(
    val modelUrls: String,
    val engine: String,
)