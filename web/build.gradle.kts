group = "com.github.jactor-rises"
version = "1.0.x-SNAPSHOT"
description = "jactor-web"

plugins {
    id("org.springframework.boot") version Versions.v3_1_4
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // internal project dependency
    implementation(project(":shared"))

    // --- misc dependencies ---
    implementation("org.webjars:bootstrap:${Versions.v5_2_3}")
    implementation("org.webjars:jquery:${Versions.v3_6_4}")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:${Versions.v1_7_0}")
}
