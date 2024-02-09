plugins {
    kotlin("jvm") version "1.8.20"

    id("io.freefair.aspectj.post-compile-weaving") version "6.6.3"
}

group = "r3nny.codest.runner"
version = "2023.0.1"

repositories {
    mavenCentral()
}

fun testcontainersVerison() = "1.19.1"

dependencies {
    implementation(project(":codest-shared"))

    aspect(project(":codest-logger"))
    implementation(project(":codest-logger"))
    implementation("com.sksamuel.hoplite:hoplite-core:2.7.5")
    implementation("com.sksamuel.hoplite:hoplite-json:2.7.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")


    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVerison()}")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.3")
    testImplementation("io.kotest:kotest-assertions-json-jvm:5.5.3")
    testImplementation("io.mockk:mockk:1.13.2")
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

kotlin {
    jvmToolchain(17)
}
