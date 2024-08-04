package com.huggingface.reranker

import ai.djl.repository.zoo.Criteria
import ai.djl.repository.zoo.ZooModel
import ai.djl.training.util.ProgressBar
import ai.djl.util.StringPair
import java.util.Scanner

fun main() {

    val criteria = Criteria.builder()
        .setTypes(StringPair::class.java, FloatArray::class.java)
        .optModelUrls("djl://ai.djl.huggingface.pytorch/BAAI/bge-reranker-v2-m3")
        .optEngine("PyTorch")
        .optArgument("reranking", "true")
        .optProgress(ProgressBar())
        .build()

    val scanner = Scanner(System.`in`)

    val model: ZooModel<StringPair, FloatArray> = criteria.loadModel()
    model
        .use { model ->
            while (true) {
                print("Enter text: ")
                val t = scanner.nextLine()

                val textList = listOf(
                    StringPair(
                        t,
                        "The giant panda (Ailuropoda melanoleuca) is a bear species native to the mountainous regions of China. Their primary habitat consists of high-altitude bamboo forests, where they can easily find their main food source. Protecting and restoring these habitats is crucial for panda conservation.",
                    ),
                    StringPair(
                        t,
                        "Pandas are primarily herbivores, with bamboo making up the majority of their diet. They spend most of their day eating, consuming various parts of the bamboo plant, including stalks, leaves, and shoots. Occasionally, they may also eat small mammals or birds.",
                    ),
                    StringPair(
                        t,
                        "Pandas play a vital ecological role within their habitat. By living in bamboo forests, they contribute to the growth and spread of bamboo, which in turn provides a home for a diverse range of species.",
                    ),
                    StringPair(
                        t,
                        "The giant panda is classified as a vulnerable species. Efforts by the Chinese government and international organizations focus on conservation strategies, including captive breeding programs and habitat protection, to ensure their survival.",
                    ),
                    StringPair(
                        t,
                        "Pandas are beloved animals worldwide and are especially recognized as a national symbol of China. They are often associated with peace and nature conservation and can be seen in zoos around the world.",
                    )
                )

                model.newPredictor().use { predictor ->
                    val res = predictor.batchPredict(textList)
                    res.forEach { println(it[0]) }
                }
            }
        }
}