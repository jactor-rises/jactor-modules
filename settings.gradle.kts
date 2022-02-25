rootProject.name = "jactor-modules"
include("persistence")
include("web")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("commonslang3", "org.apache.commons:commons-lang3:3.12.0")
            library("flyway", "org.flywaydb:flyway-core:8.5.1")
            library("h2", "com.h2database:h2:2.1.210")
            library("swagger", "org.springdoc:springdoc-openapi-ui:1.6.6")
            library("webjars-bootstrap", "org.webjars:bootstrap:5.1.3")
            library("webjars-jquery", "org.webjars:jquery:3.6.0")
        }
    }
}