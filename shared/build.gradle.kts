group = "com.github.jactor-rises"
description = "jactor::shared"

plugins {
    id("jactor-modules-kotlin-conventions")
}

val springdocVersion: String by project

dependencies {
    implementation("org.springdoc:springdoc-openapi-ui:$springdocVersion")

    // test
    testImplementation(project(":shared-test"))
}
