group = "com.github.jactor-rises"
description = "jactor::shared"

val springdocVersion: String by project

dependencies {
    // logging og swagger
    implementation("org.springdoc:springdoc-openapi-ui:$springdocVersion")
}
