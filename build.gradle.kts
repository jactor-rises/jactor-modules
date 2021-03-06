plugins {
    id("com.github.ben-manes.versions") version JactorModules.Version.benManesVersionsPlugin apply false
    id("io.spring.dependency-management") version JactorModules.Version.springDependencyPlugin apply false

    kotlin("plugin.spring") version JactorModules.Version.springKotlin apply false
}

subprojects {
    apply(plugin = "jactor-modules-kotlin-conventions")
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}
