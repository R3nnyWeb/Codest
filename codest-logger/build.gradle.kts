plugins {
    id("io.freefair.aspectj.post-compile-weaving") version "6.6.3"
    kotlin("jvm") version "1.8.20"
}

group = "org.r3nny.logger"
version = "2023.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.aspectj:aspectjrt:1.9.8")
    implementation("org.aspectj:aspectjweaver:1.9.8")

    implementation("net.logstash.logback:logstash-logback-encoder:7.4")


    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.4.8")
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-core
    implementation("ch.qos.logback:logback-core:1.4.8")

}
