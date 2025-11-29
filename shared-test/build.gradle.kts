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
    implementation(libs.kotlin.bom)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(libs.assertk)
    implementation(libs.junit.jupiter)
    implementation(libs.junit.platform.launcher)
}
