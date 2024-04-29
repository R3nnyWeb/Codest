import com.google.devtools.ksp.gradle.KspTask
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("kapt") version ("1.9.10")
    kotlin("jvm") version ("1.9.10")
    id("com.google.devtools.ksp") version ("1.9.10-1.0.13")
    id("org.openapi.generator") version "7.1.0"
    id("application")
}

group = "r3nny.codest.task"
version = "2023.0.1"

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath("ru.tinkoff.kora:openapi-generator:1.0.6")
    }
}

kotlin {
    jvmToolchain { languageVersion.set(JavaLanguageVersion.of("17")) }
    sourceSets.main { kotlin.srcDir("build/generated/ksp/main/kotlin") }
    sourceSets.test { kotlin.srcDir("build/generated/ksp/test/kotlin") }
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/source/openapi/main/kotlin")
        }
    }
}

val openApiServerGenerate = tasks.register<GenerateTask>("openApiServerGenerate") {
    generatorName.set("kora")
    group = "openapi tools"
    inputSpec.set("$projectDir/src/main/resources/openapi/server/codest-task-openapi.yaml")
    outputDir.set("$buildDir/generated/source/openapi/main/kotlin")
    apiPackage.set("r3nny.codest.task.api")
    modelPackage.set("r3nny.codest.task.model")
    openapiNormalizer.set(
        mapOf(
            "DISABLE_ALL" to "true",
        ),
    )
    configOptions.set(
        mapOf(
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "mode" to "kotlin-suspend-server",
        ),
    )
    typeMappings.set(
        mapOf(
            "date" to "java.time.LocalDate",
            "DateTime" to "java.time.LocalDateTime"
        )
    )
}

val koraBom: Configuration by configurations.creating
configurations {
    ksp.get().extendsFrom(koraBom)
    api.get().extendsFrom(koraBom)
    implementation.get().extendsFrom(koraBom)
}

fun testcontainersVerison() = "1.19.1"
val restAssuredVersion = "5.1.1"
fun kora(module: String) = "ru.tinkoff.kora:$module"
val koraVersion: String by project

dependencies {
    ksp("ru.tinkoff.kora:symbol-processors")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.15.3"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.3"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation(project(":codest-shared"))
    koraBom(platform("ru.tinkoff.kora:kora-parent:$koraVersion"))

    implementation("com.sksamuel.hoplite:hoplite-core:2.7.5")
    implementation("com.sksamuel.hoplite:hoplite-json:2.7.5")

    implementation(kora("http-server-undertow"))
    implementation(kora("json-module"))
    implementation(kora("http-client-async"))
    implementation(kora("kafka"))
    implementation(kora("config-hocon"))
    implementation(kora("database-jdbc"))
    implementation(kora("logging-logback"))
    implementation(kora("micrometer-module"))
    implementation(kora("validation-module"))
    implementation(kora("openapi-management"))
    implementation(kora("cache-caffeine"))

    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVerison()}")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.3")
    testImplementation("io.kotest:kotest-assertions-json-jvm:5.5.3")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    testImplementation("io.rest-assured:json-path:$restAssuredVersion")
}

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    withType<KspTask> {
        dependsOn(openApiServerGenerate)
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

kotlin {
    jvmToolchain(17)
}