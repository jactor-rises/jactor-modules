package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import assertk.assertThat
import assertk.assertions.isNotNull

internal class JactorPersistenceTest : AbstractSpringBootNoDirtyContextTest() {

    @Autowired
    private var commandLineRunner: CommandLineRunner? = null

    @Test
    fun `should contain bean named CommandlineRunner`() {
        assertThat(commandLineRunner).isNotNull()
    }
}