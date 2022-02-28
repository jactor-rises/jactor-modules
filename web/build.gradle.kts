group = "com.github.jactor-rises"
version = "1.0.x-SNAPSHOT"
description = "jactor-web"

dependencies {
    // spring-boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // internal dependency
    implementation(":shared")

    // --- misc dependencies ---
    implementation(libs.webjars.bootstrap)
    implementation(libs.webjars.jquery)

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}
