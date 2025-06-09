group = "com.github.jactor-rises"
version = "1.0.x-SNAPSHOT"
description = "jactor-web"

val bootstrapVersion: String by project
val jqueryVersion: String by project
val springdocVersion: String by project

plugins {
    id("org.springframework.boot") version "3.5.0"
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // internal project dependency
    implementation(project(":shared"))

    // --- web dependencies ---
    implementation("org.webjars:bootstrap:$bootstrapVersion")
    implementation("org.webjars:jquery:$jqueryVersion")
}
