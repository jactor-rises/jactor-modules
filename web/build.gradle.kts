import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.github.ben-manes.versions") version "0.42.0"

    kotlin("jvm") version "1.6.20-M1"
    kotlin("plugin.spring") version "1.6.20-M1"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

ext["assertj.version"] = "3.22.0"

dependencies {
    // spring-boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // --- dependencies ---

    implementation("com.github.jactor-rises:jactor-shared:0.3.5")
    implementation("org.webjars:bootstrap:5.1.3")
    implementation("org.webjars:jquery:3.6.0")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // test-dependencies versioned by spring-boot
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // test
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

group = "com.github.jactor-rises"
version = "1.0.x-SNAPSHOT"
description = "jactor-web"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io") // resolve maven package repository for github
}

tasks.compileKotlin {
    kotlinOptions {
        allWarningsAsErrors = true
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        lifecycle {
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }
        info.events = lifecycle.events
        info.exceptionFormat = lifecycle.exceptionFormat
    }

    // Se https://github.com/gradle/kotlin-dsl/issues/836
    addTestListener(MyTestListener())
}

class MyTestListener : TestListener {
    private val failedTests = mutableListOf<TestDescriptor>()
    private val skippedTests = mutableListOf<TestDescriptor>()

    override fun beforeSuite(suite: TestDescriptor) {}
    override fun beforeTest(testDescriptor: TestDescriptor) {}
    override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
        when (result.resultType) {
            TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
            TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
            else -> Unit
        }
    }

    override fun afterSuite(suite: TestDescriptor, result: TestResult) {
        if (suite.parent == null) { // root suite
            logger.lifecycle("")
            logger.lifecycle("/=======================================")
            logger.lifecycle("| Test result: ${result.resultType}")
            logger.lifecycle("|=======================================")

            logger.lifecycle(
                "| Test summary: ${result.testCount} tests, ${result.successfulTestCount} succeeded, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped"
            )

            failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
            skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
        }
    }

    private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
        logger.lifecycle(subject)
        forEach { test -> logger.lifecycle("\t\t${test.className}: ${test.displayName()}") }
    }

    private fun TestDescriptor.displayName() = parent?.name ?: name
}
