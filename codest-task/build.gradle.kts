plugins {
    kotlin("jvm") version "1.8.0"
}

group = "r3nny.codest.task.service"
version = "2023.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":codest-shared"))
    implementation("com.sksamuel.hoplite:hoplite-core:2.7.5")
    implementation("com.sksamuel.hoplite:hoplite-json:2.7.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.7.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}