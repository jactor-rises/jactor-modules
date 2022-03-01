object Library {
    object Version {
        // PS!
        // kotlin language version and kotlin-gradle plugin is an implementation defined in buildSrc/build.gradle.kts

        // plugins
        const val benManesVersionsPlugin = "0.42.0"
        const val springDependencyPlugin = "1.0.11.RELEASE"
        const val springPlugin = "1.6.20-M1"

        // dependencies
        const val flyway = "8.5.1"
        const val h2 = "2.1.210"
        const val mockitoKotlin = "4.0.0"
        const val swagger = "1.6.6"
        const val webjarsBootstrap = "5.1.3"
        const val webjarsJquery = "3.6.0"
    }

    object Dependencies {
        const val flyway = "org.flywaydb:flyway-core:${Version.flyway}"
        const val h2 = "com.h2database:h2:${Version.h2}"
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Version.mockitoKotlin}"
        const val springdocSwagger = "org.springdoc:springdoc-openapi-ui:${Version.swagger}"
        const val webjarsBootstrap = "org.webjars:bootstrap:${Version.webjarsBootstrap}"
        const val webjarsJquery = "org.webjars:jquery:${Version.webjarsJquery}"
    }
}
