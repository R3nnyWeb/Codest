plugins {
    kotlin("jvm") version "1.8.20"

    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"

    id("io.freefair.aspectj.post-compile-weaving") version "6.6.3"
}

group = "r3nny.codest.task"
version = "2023.0.1"

repositories {
    mavenCentral()
}

fun springVersion() = "3.1.5"
fun testcontainersVerison() = "1.19.1"
val restAssuredVersion = "5.1.1"

dependencies {
    implementation(project(":codest-shared"))


    aspect(project(":codest-logger"))
    implementation(project(":codest-logger"))
    implementation("com.sksamuel.hoplite:hoplite-core:2.7.5")
    implementation("com.sksamuel.hoplite:hoplite-json:2.7.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.springframework.boot:spring-boot-starter-web:${springVersion()}")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:${springVersion()}")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springVersion()}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    runtimeOnly("org.springframework.boot:spring-boot-devtools:${springVersion()}")

    testImplementation("org.springframework.boot:spring-boot-testcontainers:${springVersion()}")
    testImplementation("org.testcontainers:junit-jupiter:${testcontainersVerison()}")
    testImplementation("org.testcontainers:mongodb:${testcontainersVerison()}")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.3")
    testImplementation("io.kotest:kotest-assertions-json-jvm:5.5.3")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springVersion()}")
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