plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versions)
}

description = "jactor::shared"

dependencies {
    api(libs.kotlin.logging)
    api(libs.kotlin.reflect)
    api(libs.kotlin.stdlib.jdk8)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.reactor)
    api(libs.springdoc.openapi.ui)

    testImplementation(project(":shared-test"))
}
