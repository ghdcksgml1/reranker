package com.huggingface.reranker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class RerankerApplication

fun main(args: Array<String>) {
    runApplication<RerankerApplication>(*args)
}