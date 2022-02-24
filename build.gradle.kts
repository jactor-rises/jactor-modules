plugins {
    id("com.github.ben-manes.versions") version "0.42.0" apply false
}

subprojects {
    apply(plugin = "jactor-modules-kotlin-conventions")
    apply(plugin = "com.github.ben-manes.versions")
}
