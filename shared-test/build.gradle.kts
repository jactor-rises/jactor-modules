plugins {
    id("jactor-modules-spring-library")
}

group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::shared-test"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)

    api(libs.assertk)
    api(libs.junit.jupiter)
    api(libs.junit.platform.launcher)
    api(libs.junit.platform.suite)
    api(libs.kotlinx.coroutines.test)
    api(libs.mockk)
}
