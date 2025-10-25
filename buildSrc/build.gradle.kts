val kotlinVersion = "2.1.0"

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")

    // spring-boot gradle plugin
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.4.1")
}
