plugins {
    kotlin("jvm") version "1.8.0"
}

group = "r3nny.codest.executor"
version = "2023.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":codest-shared"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}