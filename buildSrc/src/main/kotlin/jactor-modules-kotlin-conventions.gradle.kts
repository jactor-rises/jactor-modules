import org.gradle.api.tasks.testing.logging.TestLogEvent

val assertkVersion: String by project
val cucumberVersion: String by project
val junitPlatformVersion: String by project
val kotlinLoggingVersion: String by project
val mockkVersion: String by project
val springBootVersion: String by project
val springdocVersion: String by project
val springmockkVersion: String by project

plugins {
    kotlin("jvm")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // springdoc-openapi (swagger)
    implementation("org.springdoc:springdoc-openapi-ui:$springdocVersion")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:${kotlinLoggingVersion}")

    // test
    testImplementation("com.ninja-squad:springmockk:$springmockkVersion")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:$assertkVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.junit.platform:junit-platform-suite:$junitPlatformVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion") {
        exclude(group = "org.assertj")
        exclude(group = "org.junit", module = "junit")
        exclude(group = "org.hamcrest")
        exclude(group = "org.mockito")
    }

    // cucumber
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-java8:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-spring:$cucumberVersion")
}

tasks.compileKotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        lifecycle {
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }

        info.events = lifecycle.events
        info.exceptionFormat = lifecycle.exceptionFormat
    }
}
