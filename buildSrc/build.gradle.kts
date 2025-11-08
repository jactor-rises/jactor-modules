plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

val toml = file("../gradle/libs.versions.toml").readText()
val versionRegex = """([\w-]+)\s*=\s*"([^"]+)"""".toRegex()
val versions = versionRegex.findAll(toml)
    .associate { it.groupValues[1] to it.groupValues[2] }

dependencies {
    implementation("io.spring.gradle:dependency-management-plugin:${versions["dependencyManagement"]}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions["kotlin"]}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${versions["kotlin"]}")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${versions["spring-boot"]}")
}
