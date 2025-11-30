package com.github.jactor.rises.web.client

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.github.jactor.rises.web.test.AbstractNoDirtySpringContextTest
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import java.net.URI

internal class UserClientIntegrationTest
    @Autowired
    constructor(
        private val userClientToTest: UserClient,
        @param:Value("\${jactor-persistence.url.root}") private val baseUrl: String,
    ) : AbstractNoDirtySpringContextTest() {
        @BeforeEach
        fun `assume jactor-persistence is running`() {
            runCatching {
                val response: ResponseEntity<String> =
                    testRestTemplate.getForEntity(
                        "$baseUrl/actuator/health",
                        String::class.java,
                    )

                assumeTrue(response.statusCode.is2xxSuccessful, response.statusCode.toString())
                assumeTrue(response.body?.contains("UP") ?: false, response.body ?: "null")
            }.onFailure { assumeTrue(false) }
        }

        @Test
        fun `should verify expected base url`() {
            val uri = restTemplate.uriTemplateHandler.expand("/user")
            assertThat(uri).isEqualTo(URI("$baseUrl/user"))
        }

        @Test
        fun `should find the default persisted user`() {
            val usernames = userClientToTest.findAllUsernames()
            assertThat(usernames).contains("tip")
        }

        @Test
        fun `should find the user named jactor`() {
            val possibleUser = userClientToTest.find("jactor")

            assertThat(possibleUser).isNotNull()
            val user = possibleUser!!

            assertThat(user.emailAddress).isEqualTo("tor.egil.jacobsen@gmail.com")
        }
    }
