group = "com.github.jactor-rises"
version = "1.0.x-SNAPSHOT"
description = "jactor-web"

plugins {
    id("org.springframework.boot") version "2.6.4"
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // internal project dependency
    implementation(project(":shared"))

    // --- misc dependencies ---
    implementation(Library.Dependencies.webjarsBootstrap)
    implementation(Library.Dependencies.webjarsJquery)

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}
