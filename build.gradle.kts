plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "com.huggingface"
version = "0.0.1-SNAPSHOT"
val djl_version = "0.29.0"

extra["springAiVersion"] = "1.0.0-M1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.spring.io/milestone")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // djl libraries
    implementation(platform("ai.djl:bom:${djl_version}"))
    implementation("ai.djl:api")
    implementation("ai.djl.huggingface:tokenizers")

    runtimeOnly("ai.djl.pytorch:pytorch-engine")
    runtimeOnly("ai.djl.pytorch:pytorch-model-zoo")
    runtimeOnly("ai.djl.pytorch:pytorch-native-cpu:1.13.1:osx-x86_64")
    runtimeOnly("ai.djl.pytorch:pytorch-jni:1.13.1-0.29.0")

    // spring-ai
    implementation("org.springframework.ai:spring-ai-ollama-spring-boot-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
