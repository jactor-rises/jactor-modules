group = "com.github.jactor-rises"
description = "jactor::shared-test"

plugins {
    id("jactor-modules-kotlin-conventions")
}

val assertkVersion: String by project
val junitPlatformVersion: String by project
val mockkVersion: String by project

dependencies {
    implementation("com.willowtreeapps.assertk:assertk-jvm:$assertkVersion")
    implementation("io.mockk:mockk:$mockkVersion")
    implementation("org.junit.platform:junit-platform-suite:$junitPlatformVersion")
}
