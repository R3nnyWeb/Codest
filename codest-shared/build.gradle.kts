plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("kapt") version "1.8.20"
}

group = "r3nny.codest.executor"
version = "2023.0.1"

repositories {
    mavenCentral()
    google()
}

  val agent: Configuration by configurations.creating

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.auto.service:auto-service:1.0.1")
    kapt("com.google.auto.service:auto-service:1.0.1")



    agent("org.aspectj:aspectjweaver:1.9.4")
    compileOnly("org.aspectj:aspectjrt:1.9.4")
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()


    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}

kotlin {
    jvmToolchain(17)
}