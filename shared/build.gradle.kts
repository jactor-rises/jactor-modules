group = "com.github.jactor-rises"
description = "jactor::shared"

val assertkVersion: String by project
val junitPlatformVersion: String by project
val mockkVersion: String by project
val springBootVersion: String = "3.5.0"
val springdocVersion: String by project

dependencies {
    implementation("org.springdoc:springdoc-openapi-ui:$springdocVersion")

    // test
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:$assertkVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.junit.platform:junit-platform-suite:$junitPlatformVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion") {
        exclude(group = "org.assertj", module = "assertj-core")
        exclude(group = "org.junit", module = "junit")
        exclude(group = "org.hamcrest")
        exclude(group = "org.mockito")
    }
}
