plugins {
    id("jactor-modules-spring-library")
}

group = "com.github.jactor-rises"
version = "2.0.x-SNAPSHOT"
description = "jactor::shared-test"

val assertkVersion: String by project

dependencies {
    implementation("com.willowtreeapps.assertk:assertk-jvm:$assertkVersion")
}
