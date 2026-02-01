plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.versions)
    alias(libs.plugins.spring.boot)
}

description = "jactor::web"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // internal project dependencies
    implementation(project(":shared"))

    // misc third party dependencies
    implementation(libs.kotlin.logging)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)

    // test implementations
    testImplementation(libs.assertk)
    testImplementation(libs.springmockk)
    testImplementation(libs.spring.boot.starter.test)
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}
