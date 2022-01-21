plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.github.ben-manes.versions") version "0.41.0"

    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // --- dependencies ---

    implementation("com.github.jactor-rises:jactor-shared:0.3.5")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    // database
    implementation("org.flywaydb:flyway-core:8.4.2")
    runtimeOnly("com.h2database:h2:2.1.210")

    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.6.4")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

tasks.compileKotlin {
    kotlinOptions {
        allWarningsAsErrors = true
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}
