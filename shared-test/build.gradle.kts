plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.versions)
}

description = "jactor::shared-test"

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
