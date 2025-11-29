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

val toml = file("../gradle/libs.versions.toml").readText()
val versionRegex = """([\w-]+)\s*=\s*"([^"]+)"""".toRegex()
val versions = versionRegex.findAll(toml)
    .associate { it.groupValues[1] to it.groupValues[2] }

dependencies {
    // kotlin coroutines bom
    add("implementation", platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:${versions["coroutines"]}"))

    // kotlin
    add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core")
    add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // cucumber
    add("testImplementation", "io.cucumber:cucumber-java:${versions["cucumber"]}")
    add("testImplementation", "io.cucumber:cucumber-java8:${versions["cucumber"]}")
    add("testImplementation", "io.cucumber:cucumber-junit-platform-engine:${versions["cucumber"]}")
    add("testImplementation", "io.cucumber:cucumber-spring:${versions["cucumber"]}")
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
