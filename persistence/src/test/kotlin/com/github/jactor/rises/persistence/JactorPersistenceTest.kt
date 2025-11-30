package com.github.jactor.rises.persistence

import assertk.assertThat
import assertk.assertions.isNotNull
import com.github.jactor.rises.persistence.test.AbstractSpringBootNoDirtyContextTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner

internal class JactorPersistenceTest @Autowired constructor(
    private val commandLineRunner: CommandLineRunner?,
) : AbstractSpringBootNoDirtyContextTest() {
    @Test
    fun `should contain bean named CommandlineRunner`() {
        assertThat(commandLineRunner).isNotNull()
    }
}
