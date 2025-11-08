import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Apply plugins imperatively (not using plugins {} block)
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "java-library")
apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

val assertkVersion: String by project
val cucumberVersion: String by project
val coroutinesVersion: String by project
val junitPlatformVersion: String by project
val kotlinLoggingVersion: String by project
val mockkVersion: String by project
val springdocVersion: String by project
val springmockkVersion: String by project

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

dependencies {
    // kotlin coroutines bom
    add("implementation", platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$coroutinesVersion"))

    // kotlin
    add("implementation", "org.jetbrains.kotlin:kotlin-reflect")
    add("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core")
    add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // springdoc-openapi (swagger)
    add("implementation", "org.springdoc:springdoc-openapi-ui:$springdocVersion")

    // logging
    add("implementation", "io.github.oshai:kotlin-logging-jvm:${kotlinLoggingVersion}")

    // internal test dependencies
    add("testImplementation", project(":shared-test"))

    // test
    add("testImplementation", "com.ninja-squad:springmockk:$springmockkVersion")
    add("testImplementation", "com.willowtreeapps.assertk:assertk-jvm:$assertkVersion")
    add("testImplementation", "io.mockk:mockk:$mockkVersion")
    add("testImplementation", "org.jetbrains.kotlin:kotlin-test-junit5")
    add("testImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test")
    add("testImplementation", "org.junit.platform:junit-platform-suite:$junitPlatformVersion")
    add("testImplementation", "org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.assertj")
        exclude(group = "org.junit", module = "junit")
        exclude(group = "org.hamcrest")
        exclude(group = "org.mockito")
    }

    // cucumber
    add("testImplementation", "io.cucumber:cucumber-java:$cucumberVersion")
    add("testImplementation", "io.cucumber:cucumber-java8:$cucumberVersion")
    add("testImplementation", "io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    add("testImplementation", "io.cucumber:cucumber-spring:$cucumberVersion")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        allWarningsAsErrors = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}
