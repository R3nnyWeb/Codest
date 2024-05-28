import com.google.devtools.ksp.gradle.KspTask
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("kapt") version ("1.9.10")
    kotlin("jvm") version ("1.9.10")
    id("com.google.devtools.ksp") version ("1.9.10-1.0.13")
    id("org.openapi.generator") version "7.1.0"
    id("application")
}

group = "r3nny.codest.user.api"
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
    jvmToolchain { languageVersion.set(JavaLanguageVersion.of("19")) }
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

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/source/openapi/main/kotlin")
        }
    }
}


fun kora(module: String) = "ru.tinkoff.kora:$module"
val koraVersion: String by project

dependencies {
    implementation(project(":codest-shared"))
    koraBom(platform("ru.tinkoff.kora:kora-parent:$koraVersion"))

    ksp("ru.tinkoff.kora:symbol-processors")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    implementation("at.favre.lib:bcrypt:0.10.2")

    implementation("org.postgresql:postgresql:42.7.3")

    implementation(kora("http-server-undertow"))
    implementation(kora("json-module"))
    implementation(kora("config-hocon"))
    implementation(kora("logging-common"))
    implementation(kora("logging-logback"))
    implementation(kora("micrometer-module"))
    implementation(kora("database-jdbc"))
    implementation(kora("openapi-management"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.3")
    testImplementation("io.kotest:kotest-assertions-json-jvm:5.5.3")
    testImplementation("io.mockk:mockk:1.13.2")
}

val openApiServerGenerate = tasks.register<GenerateTask>("openApiServerGenerate") {
    generatorName.set("kora")
    group = "openapi tools"
    inputSpec.set("$projectDir/src/main/resources/openapi/server/user-api-openapi.yaml")
    outputDir.set("$buildDir/generated/source/openapi/main/kotlin")
    apiPackage.set("r3nny.codest.user.api.api")
    modelPackage.set("r3nny.codest.user.api.model")
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

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(19))
        }
    }

    withType<KspTask> {
        dependsOn(openApiServerGenerate)
    }

    distTar {
        archiveFileName.set("app.tar")
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "19"
            freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xjsr305=strict")
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "19"
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

