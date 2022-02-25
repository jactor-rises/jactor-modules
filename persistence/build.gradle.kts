dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // --- misc dependencies ---
    implementation("org.apache.commons:commons-lang3:3.12.0")

    // database
    runtimeOnly("org.flywaydb:flyway-core:8.5.1")
    runtimeOnly("com.h2database:h2:2.1.210")

    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
}

group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"
