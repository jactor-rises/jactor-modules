package com.github.jactor.web.consumer

import com.github.jactor.shared.dto.UserDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.net.URI

@DisplayName("The UserConsumer")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
internal class UserConsumerIntegrationTest {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    @Qualifier("userConsumer")
    private lateinit var userConsumerToTest: UserConsumer

    @Value("\${jactor-persistence.url.root}")
    private lateinit var baseUrl: String

    @BeforeEach
    fun `assume jactor-persistence is running`() {
        lateinit var response: ResponseEntity<String>

        try {
            response = testRestTemplate.getForEntity("$baseUrl/actuator/health", String::class.java)
        } catch (e: RestClientException) {
            assumeTrue(false, "Failure with rest api: " + e.message)
        }

        assumeTrue(response.statusCode.is2xxSuccessful, response.statusCode.reasonPhrase)
        assumeTrue(response.body?.contains("UP") ?: false, response.body)
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

        assertAll(
            { assertThat(possibleUser).`as`("possible user").isPresent },
            {
                val user = possibleUser.orElse(UserDto())

                assertAll(
                    { assertThat(user.person?.firstName).`as`("user.person.firstName").isEqualTo("Tor Egil") },
                    { assertThat(user.emailAddress).`as`("user.emailaddress").isEqualTo("tor.egil.jacobsen@gmail.com") }
                )
            }
        )
    }
}
