
plugins {
    id("com.github.ben-manes.versions") version "0.46.0" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false

    kotlin("plugin.spring") version "2.2.10" apply false
}

subprojects {
    apply(plugin = "jactor-modules-kotlin-conventions")
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}
