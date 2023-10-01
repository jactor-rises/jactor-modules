import org.gradle.api.tasks.testing.logging.TestLogEvent

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
    implementation("org.springdoc:springdoc-openapi-ui:${Versions.V1_7_0}")

    // test
    testImplementation("com.ninja-squad:springmockk:${Versions.V4_0_2}")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.27.0")
    testImplementation("io.mockk:mockk:${Versions.V1_13_8}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks {
    compileKotlin {
        kotlinOptions {
            allWarningsAsErrors = true
        }
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
