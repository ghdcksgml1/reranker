package com.huggingface.reranker

import ai.djl.repository.zoo.ZooModel
import ai.djl.util.StringPair
import org.springframework.stereotype.Component

@Component
class Reranker(
    val rerankerModel: ZooModel<StringPair, FloatArray>,
) {

    suspend fun rerank(request: RerankRequest): RerankResponse {
        val response = request.documents.map { document ->
            StringPair(request.message, document)
        }.let { inputs ->
            rerankerModel.newPredictor().use { predictor ->

                return@let predictor.batchPredict(inputs).mapIndexed { index, output ->
                    val probability: Float = output[0]
                    DocumentProbability(document = request.documents[index], probability = probability)
                }
            }
        }.let {
            val rerankedDocuments: List<DocumentProbability> = it.sortedByDescending(DocumentProbability::probability)
            RerankResponse(message = request.message, documentProbabilityList = rerankedDocuments)
        }

        return response
    }
}