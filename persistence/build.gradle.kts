group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

plugins {
    id("org.springframework.boot") version JactorModules.Version.springBoot
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
    runtimeOnly(JactorModules.Dependencies.flyway)
    runtimeOnly(JactorModules.Dependencies.h2)

    // swagger
    implementation(JactorModules.Dependencies.springdocOpenApi)
}
