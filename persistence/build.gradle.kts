group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

plugins {
    id("org.springframework.boot") version Versions.v3_1_3
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // internal project dependency
    implementation(project(":shared"))

    // database
    runtimeOnly("org.flywaydb:flyway-core:${Versions.v9_22_0}")
    runtimeOnly("com.h2database:h2:${Versions.v2_2_222}")

    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:${Versions.v1_7_0}")
}
