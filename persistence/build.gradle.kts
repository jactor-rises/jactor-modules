plugins {
    id("jactor-modules-spring-application")
}

group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // jetbrains-exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.java.time)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.spring.boot.starter)

    // internal project dependency
    implementation(project(":shared"))

    // runtime dependencies
    runtimeOnly(libs.flyway.core)
    runtimeOnly(libs.h2database)
}

tasks.test {
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
    exclude("**/RunCucumberTest*")
}
