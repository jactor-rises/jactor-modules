plugins {
    `kotlin-dsl`
    alias(libs.plugins.versions)
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

allprojects {
    group = "com.github.jactor-rises"
    version = "2.0.x-SNAPSHOT"

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

subprojects {
    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(libs.versions.jvm.get().toInt()))
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            allWarningsAsErrors = true
            freeCompilerArgs.add("-Xjsr305=strict")
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

    plugins.withId("org.springframework.boot") {
        tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
            enabled = false
        }
        tasks.named<Jar>("jar") {
            enabled = true
        }
    }
}
