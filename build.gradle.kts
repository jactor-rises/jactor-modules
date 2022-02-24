plugins {
    kotlin("jvm") version "1.6.20-M1" apply false
    id("com.github.ben-manes.versions") version "0.42.0" apply false
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://jitpack.io")
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.ben-manes.versions")
}
