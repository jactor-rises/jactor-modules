plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
}
