plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.versions)
}

description = "jactor::web"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // internal project dependencies
    implementation(project(":shared"))
    testImplementation(project(":shared-test"))

    // test implementations
    testImplementation(libs.springmockk)
    testImplementation(libs.spring.boot.starter.test)
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}
