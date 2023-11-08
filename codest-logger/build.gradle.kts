plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "org.r3nny"
version = "2023.0.1"

repositories {
    mavenCentral()
}

val agent: Configuration by configurations.creating

dependencies {
    agent("org.aspectj:aspectjweaver:1.9.7")
    compileOnly("org.aspectj:aspectjrt:1.9.7")
    implementation(project(":codest-shared"))
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

application{
    mainClass.set("r3nny.codest.logging.aspect.MainKt")

    applicationDefaultJvmArgs += listOf("-javaagent:${agent.singleFile}")
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