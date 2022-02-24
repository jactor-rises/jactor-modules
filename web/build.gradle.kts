plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.github.ben-manes.versions") version "0.42.0"

    kotlin("plugin.spring") version "1.6.20-M1"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

ext["assertj.version"] = "3.22.0"

dependencies {
    // spring-boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // --- dependencies ---

    implementation("com.github.jactor-rises:jactor-shared:0.3.5")
    implementation("org.webjars:bootstrap:5.1.3")
    implementation("org.webjars:jquery:3.6.0")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // test-dependencies versioned by spring-boot
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // test
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

group = "com.github.jactor-rises"
version = "1.0.x-SNAPSHOT"
description = "jactor-web"
