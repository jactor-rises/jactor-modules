plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.versions)
}

description = "jactor::persistence"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // jetbrains-exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.java.time)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.spring.boot.starter)
    implementation(libs.uuid.generator)

    // internal project dependencies
    implementation(project(":shared"))
    testImplementation(project(":shared-test"))

    // runtime dependencies
    runtimeOnly(libs.flyway.core)
    runtimeOnly(libs.h2database)

    // test implementations
    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.java8)
    testImplementation(libs.cucumber.junit.platform.engine)
    testImplementation(libs.cucumber.spring)
    testImplementation(libs.springmockk)
    testImplementation(libs.spring.boot.starter.test)
}

tasks.test {
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
    exclude("**/RunCucumberTest*")
}
