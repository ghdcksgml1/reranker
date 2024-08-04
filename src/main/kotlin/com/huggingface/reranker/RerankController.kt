package com.huggingface.reranker

import ai.djl.util.StringPair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RerankController(
    val reranker: Reranker,
    val ollamaChatModel: OllamaChatModel,
) {

    @GetMapping("/rerank/{message}")
    suspend fun rerank(@PathVariable message: String) = flow {
        val documents = listOf(
            "The giant panda (Ailuropoda melanoleuca) is a bear species native to the mountainous regions of China. Their primary habitat consists of high-altitude bamboo forests, where they can easily find their main food source. Protecting and restoring these habitats is crucial for panda conservation.",
            "Pandas are primarily herbivores, with bamboo making up the majority of their diet. They spend most of their day eating, consuming various parts of the bamboo plant, including stalks, leaves, and shoots. Occasionally, they may also eat small mammals or birds.",
            "Pandas play a vital ecological role within their habitat. By living in bamboo forests, they contribute to the growth and spread of bamboo, which in turn provides a home for a diverse range of species.",
            "The giant panda is classified as a vulnerable species. Efforts by the Chinese government and international organizations focus on conservation strategies, including captive breeding programs and habitat protection, to ensure their survival.",
            "Pandas are beloved animals worldwide and are especially recognized as a national symbol of China. They are often associated with peace and nature conservation and can be seen in zoos around the world.",
        )
        val request: RerankRequest = RerankRequest(message = message, documents = documents)
        val response: RerankResponse = reranker.rerank(request)
        val bestDocument = response.documentProbabilityList.first().document

        println("유사도 : ${response.documentProbabilityList.first().probability}")

        val chat = ollamaChatModel.call(
            SystemMessage("your useful chatbot. input area is user input message and documents area is that information you should refer to."),
            UserMessage("input: $message"),
            AssistantMessage("documents: $bestDocument")
        )
        println(chat)
        emit(chat)
//            .asFlow()
//            .flowOn(Dispatchers.IO)
//            .onStart { println("Reranker start") }
//            .onEach { print(it) }
//            .onCompletion { println("Reranker end") }
//            .collect { emit(it) }
    }
}