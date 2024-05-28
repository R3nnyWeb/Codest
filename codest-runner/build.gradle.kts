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


fun testcontainersVerison() = "1.19.1"

fun kora(module: String) = "ru.tinkoff.kora:$module"
val koraVersion: String by project

dependencies {
    implementation(project(":codest-shared"))
    koraBom(platform("ru.tinkoff.kora:kora-parent:$koraVersion"))

    ksp("ru.tinkoff.kora:symbol-processors")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")


    implementation(kora("http-server-undertow"))
    implementation(kora("json-module"))
    implementation(kora("config-hocon"))
    implementation(kora("logging-common"))
    implementation(kora("logging-logback"))
    implementation(kora("micrometer-module"))
    implementation(kora("kafka"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")

    testImplementation(kora("test-junit5"))
    testImplementation("io.goodforgod:testcontainers-extensions-kafka:0.9.6")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVerison()}")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.3")
    testImplementation("io.kotest:kotest-assertions-json-jvm:5.5.3")
    testImplementation("io.mockk:mockk:1.13.2")
}

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(19))
        }
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
    archiveFileName.set("app.tar")
}

