group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

plugins {
    id("org.springframework.boot") version "3.3.5"
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
    runtimeOnly("org.flywaydb:flyway-core:10.20.1")
    runtimeOnly("com.h2database:h2:2.3.232")
}

tasks.test {
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
}
