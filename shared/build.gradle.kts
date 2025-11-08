plugins {
    id("jactor-modules-spring-library")
}

group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::shared"
val javaVersion = libs.versions.jvm.get().toInt()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }

    jvmToolchain(21)  // Use same version as Java
}

dependencies {
    testImplementation(project(":shared-test"))
}
