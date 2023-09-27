group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

plugins {
    id("org.springframework.boot") version Versions.V3_1_4
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
    runtimeOnly("org.flywaydb:flyway-core:${Versions.V9_22_0}")
    runtimeOnly("com.h2database:h2:${Versions.V2_2_224}")

    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:${Versions.V1_7_0}")
}
