plugins {
    kotlin("kapt") version ("1.9.10")
    kotlin("jvm") version ("1.9.10")
    id("com.google.devtools.ksp") version ("1.9.10-1.0.13")
    id("application")
}

group = "r3nny.codest.runner"
version = "2023.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain { languageVersion.set(JavaLanguageVersion.of("17")) }
    sourceSets.main { kotlin.srcDir("build/generated/ksp/main/kotlin") }
    sourceSets.test { kotlin.srcDir("build/generated/ksp/test/kotlin") }
}

val koraBom: Configuration by configurations.creating
configurations {
    ksp.get().extendsFrom(koraBom)
    api.get().extendsFrom(koraBom)
    implementation.get().extendsFrom(koraBom)
}

application {
    mainClass.set("r3nny.codest.runner.AppKt")
}


fun testcontainersVerison() = "1.19.1"


val koraVersion: String by project
dependencies {
    implementation(project(":codest-shared"))
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVerison()}")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.3")
    testImplementation("io.kotest:kotest-assertions-json-jvm:5.5.3")
    testImplementation("io.mockk:mockk:1.13.2")
    koraBom(platform("ru.tinkoff.kora:kora-parent:1.0.8"))

    ksp("ru.tinkoff.kora:symbol-processors")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")


    implementation("ru.tinkoff.kora:http-server-undertow")
    implementation("ru.tinkoff.kora:json-module")
    implementation("ru.tinkoff.kora:config-hocon")
    implementation("ru.tinkoff.kora:logging-common")
    implementation("ru.tinkoff.kora:logging-logback")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")
    implementation("ru.tinkoff.kora:micrometer-module")
    implementation("ru.tinkoff.kora:kafka")

    testImplementation("ru.tinkoff.kora:test-junit5")
    testImplementation("io.goodforgod:testcontainers-extensions-kafka:0.9.6")


}

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xjsr305=strict")
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xjsr305=strict")
        }
    }

    test {
        useJUnitPlatform()
    }
}

tasks.distTar {
    archiveFileName.set("application.tar")
}

