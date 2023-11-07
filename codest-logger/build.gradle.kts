plugins {
    kotlin("jvm") version "1.8.20"
}

group = "org.r3nny"
version = "2023.0.1"

repositories {
    mavenCentral()
}

val agent: Configuration by configurations.creating

dependencies {
    agent("org.aspectj:aspectjweaver:1.9.4")
    compileOnly("org.aspectj:aspectjrt:1.9.4")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
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


}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()

    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}

kotlin {
    jvmToolchain(17)
}