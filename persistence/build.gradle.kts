
group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

val flywayVersion: String by project
val h2DatabaseVersion: String by project
val kotlinLoggingVersion: String by project

plugins {
    id("org.springframework.boot") version "3.5.0"
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // internal project dependency
    implementation(project(":shared"))

    // misc dependencies
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")

    // runtime dependencies
    runtimeOnly("org.flywaydb:flyway-core:$flywayVersion")
    runtimeOnly("com.h2database:h2:$h2DatabaseVersion")
}

tasks.test {
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
}
