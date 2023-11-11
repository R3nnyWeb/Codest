plugins {
    kotlin("jvm") version "1.8.20"
}

group = "r3nny.codest.executor"
version = "2023.0.1"

repositories {
    mavenCentral()
}

  val agent: Configuration by configurations.creating

dependencies {


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()

}

kotlin {
    jvmToolchain(17)
}