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
    add("implementation", "org.jetbrains.kotlin:kotlin-reflect")
    add("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core")
    add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // springdoc-openapi (swagger)
    add("implementation", "org.springdoc:springdoc-openapi-ui:${versions["springdoc-openapi"]}")

    // logging
    add("implementation", "io.github.oshai:kotlin-logging-jvm:${versions["kotlin-logging"]}")

    // internal test dependencies
    add("testImplementation", project(":shared-test"))

    // test
    add("testImplementation", "com.willowtreeapps.assertk:assertk-jvm:${versions["assertk"]}")
    add("testImplementation", "io.mockk:mockk:${versions["mockk"]}")
    add("testImplementation", "org.jetbrains.kotlin:kotlin-test-junit5")
    add("testImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test")
    add("testImplementation", "org.junit.platform:junit-platform-suite:${versions["junitPlatform"]}")
    add("testImplementation", "org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.assertj")
        exclude(group = "org.junit", module = "junit")
        exclude(group = "org.hamcrest")
        exclude(group = "org.mockito")
    }

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
