group = "com.github.jactor-rises"
version = "1.0.x-SNAPSHOT"
description = "jactor-web"

plugins {
    id("org.springframework.boot") version "3.2.1"
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // internal project dependency
    implementation(project(":shared"))

    // --- misc dependencies ---
    implementation("org.webjars:bootstrap:5.3.2")
    implementation("org.webjars:jquery:3.7.1")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
}
