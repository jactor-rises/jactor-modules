package com.github.jactor.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.context.SpringBootTest

internal class JactorPersistenceTest: AbstractSpringBootNoDirtyContextTest() {

    @Autowired
    private lateinit var commandLineRunner: CommandLineRunner

    @Test
    fun `should contain bean named CommandlineRunner`() {
        assertThat(commandLineRunner).isNotNull
    }
}