plugins {
    id("com.github.ben-manes.versions") version "0.42.0" apply false
    id("org.springframework.boot") version "2.6.4" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false

    kotlin("plugin.spring") version "1.6.20-M1" apply false
}

subprojects {
    apply(plugin = "jactor-modules-kotlin-conventions")
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}
