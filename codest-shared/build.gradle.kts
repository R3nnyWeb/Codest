plugins {
    kotlin("jvm") version ("1.9.10")
    id("com.google.devtools.ksp") version ("1.9.10-1.0.13")
    id("application")
}

group = "r3nny.codest.executor"
version = "2023.0.1"

kotlin {
    jvmToolchain { languageVersion.set(JavaLanguageVersion.of("17")) }
    sourceSets.main { kotlin.srcDir("build/generated/ksp/main/kotlin") }
    sourceSets.test { kotlin.srcDir("build/generated/ksp/test/kotlin") }
}

repositories {
    mavenCentral()
}

val agent: Configuration by configurations.creating

val koraBom: Configuration by configurations.creating
configurations {
    ksp.get().extendsFrom(koraBom)
    api.get().extendsFrom(koraBom)
    implementation.get().extendsFrom(koraBom)
}

dependencies {
    ksp("ru.tinkoff.kora:symbol-processors")
    koraBom(platform("ru.tinkoff.kora:kora-parent:1.0.8"))
    implementation("ru.tinkoff.kora:json-module")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()

}

kotlin {
    jvmToolchain(17)
}