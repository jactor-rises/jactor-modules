import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Apply plugins imperatively (not using plugins {} block)
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "java-library")
apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
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
