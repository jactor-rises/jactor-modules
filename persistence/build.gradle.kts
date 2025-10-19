group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

val exposedVersion: String by project
val flywayVersion: String by project
val h2DatabaseVersion: String by project

plugins {
    id("org.springframework.boot") version "3.5.5"
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // internal project dependency
    implementation(project(":shared"))

    // jetbrains-exposed
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")

    // runtime dependencies
    runtimeOnly("org.flywaydb:flyway-core:$flywayVersion")
    runtimeOnly("com.h2database:h2:$h2DatabaseVersion")
}

tasks.test {
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
    exclude("**/RunCucumberTest*")
}
