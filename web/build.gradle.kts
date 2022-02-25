repositories {
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

ext["assertj.version"] = "3.22.0"

dependencies {
    // spring-boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // --- misc dependencies ---
    implementation("org.webjars:bootstrap:5.1.3")
    implementation("org.webjars:jquery:3.6.0")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

group = "com.github.jactor-rises"
version = "1.0.x-SNAPSHOT"
description = "jactor-web"
