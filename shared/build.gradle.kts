plugins {
    id("jactor-modules-spring-library")
}

group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::shared"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

val javaVersion = libs.versions.jvm.get().toInt()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }

    jvmToolchain(21)  // Use same version as Java
}

dependencies {
    api(libs.kotlin.bom)
    api(libs.kotlin.logging)
    api(libs.kotlin.reflect)
    api(libs.kotlin.stdlib.jdk8)

    testImplementation(project(":shared-test"))
}
