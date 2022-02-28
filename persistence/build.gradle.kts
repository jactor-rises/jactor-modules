group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::persistence"

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // internal dependency
    implementation(":shared")

    // database
    runtimeOnly(libs.h2)
    runtimeOnly(libs.flyway)

    // swagger
    implementation(libs.swagger)
}
