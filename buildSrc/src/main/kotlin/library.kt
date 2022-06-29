object JactorModules {
    object Version {
        // PS!
        // kotlin language version and kotlin-gradle plugin is an implementation defined in buildSrc/build.gradle.kts

        // plugins
        const val benManesVersionsPlugin = "0.42.0"
        const val springDependencyPlugin = "1.0.11.RELEASE"
        const val springBoot = "2.6.7"
        const val springKotlin = "1.6.20"

        // dependencies
        const val flyway = "8.5.13"
        const val h2 = "2.1.214"
        const val mockk = "1.12.4"
        const val springdocOpenApi = "1.6.8"
        const val springmockk = "3.1.1"
        const val webjarsBootstrap = "5.1.3"
        const val webjarsJquery = "3.6.0"
    }

    object Dependencies {
        const val flyway = "org.flywaydb:flyway-core:${Version.flyway}"
        const val h2 = "com.h2database:h2:${Version.h2}"
        const val mockk = "io.mockk:mockk:${Version.mockk}"
        const val springdocOpenApi = "org.springdoc:springdoc-openapi-ui:${Version.springdocOpenApi}"
        const val springmock = "com.ninja-squad:springmockk:${Version.springmockk}"
        const val webjarsBootstrap = "org.webjars:bootstrap:${Version.webjarsBootstrap}"
        const val webjarsJquery = "org.webjars:jquery:${Version.webjarsJquery}"
    }
}
