package com.huggingface.reranker

data class RerankResponse(
    val message: String,
    val documentProbabilityList: List<DocumentProbability>,
)

data class DocumentProbability(
    val document: String,
    val probability: Float,
)