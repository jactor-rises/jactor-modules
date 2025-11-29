import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Apply plugins imperatively (not using plugins {} block)
apply(plugin = "io.spring.dependency-management")
apply(plugin = "java-library")
apply(plugin = "org.jetbrains.kotlin.jvm")

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

plugins.withId("org.springframework.boot") {
    tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = false
    }
    tasks.named<Jar>("jar") {
        enabled = true
    }
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
