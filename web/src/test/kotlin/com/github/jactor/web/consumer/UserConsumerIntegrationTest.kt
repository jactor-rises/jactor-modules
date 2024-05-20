package com.github.jactor.web.consumer

import java.net.URI
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import com.github.jactor.web.test.AbstractNoDirtySpringContextTest
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull

internal class UserConsumerIntegrationTest : AbstractNoDirtySpringContextTest() {

    @Autowired
    @Qualifier("userConsumer")
    private lateinit var userConsumerToTest: UserConsumer

    @Value("\${jactor-persistence.url.root}")
    private lateinit var baseUrl: String

    @BeforeEach
    fun `assume jactor-persistence is running`() {
        runCatching {
            val response: ResponseEntity<String> = testRestTemplate.getForEntity(
                "$baseUrl/actuator/health", String::class.java
            )

            assumeTrue(response.statusCode.is2xxSuccessful, response.statusCode.toString())
            assumeTrue(/* assumption = */ response.body?.contains("UP") ?: false, /* message = */ response.body)
        }.onFailure { assumeTrue(false) }
    }

    @Test
    fun `should verify expected base url`() {
        val uri = restTemplate.uriTemplateHandler.expand("/user")
        assertThat(uri).isEqualTo(URI("$baseUrl/user"))
    }

    @Test
    fun `should find the default persisted user`() {
        val usernames = userConsumerToTest.findAllUsernames()
        assertThat(usernames).contains("tip")
    }

    @Test
    fun `should find the user named jactor`() {
        val possibleUser = userConsumerToTest.find("jactor")

        assertThat(possibleUser).isNotNull()
        val user = possibleUser!!

        assertAll {
            assertThat(user.person?.firstName).isEqualTo("Tor Egil")
            assertThat(user.emailAddress).isEqualTo("tor.egil.jacobsen@gmail.com")
        }
    }
}
