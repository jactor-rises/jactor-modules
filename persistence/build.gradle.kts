plugins {
    id("jactor-modules-spring-application")
}

group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

val exposedVersion: String by project
val flywayVersion: String by project
val h2DatabaseVersion: String by project
val kotlinVersion: String by project

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
        force("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        force("org.jetbrains.kotlin:kotlin-stdlib-common:$kotlinVersion")
    }
}


dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // jetbrains-exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")

    // Force kotlinx-datetime version compatible with Kotlin 2.1.0
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    // Explicitly add Kotlin stdlib for both compile and runtime
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    // internal project dependency
    implementation(project(":shared"))

    // runtime dependencies
    runtimeOnly("org.flywaydb:flyway-core:$flywayVersion")
    runtimeOnly("com.h2database:h2:$h2DatabaseVersion")
}

tasks.test {
    jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
    exclude("**/RunCucumberTest*")
}
