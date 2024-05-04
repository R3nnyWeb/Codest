plugins {
    kotlin("jvm") version ("1.9.10")
    id("com.google.devtools.ksp") version ("1.9.10-1.0.13")
    id("application")
}

group = "r3nny.codest.shared"
version = "2023.0.1"

kotlin {
    jvmToolchain { languageVersion.set(JavaLanguageVersion.of("19")) }
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

fun kora(module: String) = "ru.tinkoff.kora:$module"
val koraVersion: String by project
dependencies {
    ksp("ru.tinkoff.kora:symbol-processors")
    koraBom(platform("ru.tinkoff.kora:kora-parent:$koraVersion"))
    implementation(kora("http-server-undertow"))
    implementation(kora("json-module"))
    implementation(kora("config-hocon"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
}

tasks.test {
    useJUnitPlatform()

}

kotlin {
    jvmToolchain(19)
}