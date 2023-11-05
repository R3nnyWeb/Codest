plugins {
    kotlin("jvm") version "1.8.0"
}

group = "r3nny.codest.executor"
version = "2023.0.1"

repositories {
    mavenCentral()
}

val agent: Configuration by configurations.creating

dependencies {
    agent("org.aspectj:aspectjweaver:1.9.4")
    compileOnly("org.aspectj:aspectjrt:1.9.4")



    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}