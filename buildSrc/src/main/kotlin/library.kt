object JactorModules {
    object Version {
        // PS!
        // kotlin language version and kotlin-gradle plugin is an implementation defined in buildSrc/build.gradle.kts

        // plugins
        const val benManesVersionsPlugin = "0.46.0"
        const val springDependencyPlugin = "1.1.0"
        const val springBoot = "3.1.3"
        const val springKotlin = "1.9.10"

        // dependencies
        const val flyway = "9.22.0"
        const val h2 = "2.2.222"
        const val mockk = "1.13.7"
        const val springdocOpenApi = "1.7.0"
        const val springmockk = "4.0.2"
        const val webjarsBootstrap = "5.2.3"
        const val webjarsJquery = "3.6.4"
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
