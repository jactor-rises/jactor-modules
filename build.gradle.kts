
plugins {
    id("com.github.ben-manes.versions") version Versions.v0_46_0 apply false
    id("io.spring.dependency-management") version Versions.v1_1_0 apply false

    kotlin("plugin.spring") version Versions.v1_9_10 apply false
}

subprojects {
    apply(plugin = "jactor-modules-kotlin-conventions")
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}
