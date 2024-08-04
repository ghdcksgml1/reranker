package com.huggingface.reranker

data class RerankRequest(
    val message: String,
    val documents: List<String>,
)