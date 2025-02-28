group = "com.github.jactor-rises"
description = "jactor::shared"

val kotlinLoggingVersion: String by project
val springdocVersion: String by project

dependencies {
    // logging og swagger
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")
    implementation("org.springdoc:springdoc-openapi-ui:$springdocVersion")
}
