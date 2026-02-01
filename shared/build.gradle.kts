plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versions)
}

description = "jactor::shared"

dependencies {
    implementation(libs.springdoc.openapi.ui)

    testImplementation(libs.assertk)
    testImplementation(libs.junit.platform.suite)
    testImplementation(libs.junit.jupiter)
}
