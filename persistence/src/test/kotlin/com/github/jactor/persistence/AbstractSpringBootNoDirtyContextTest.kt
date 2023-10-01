package com.github.jactor.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jactor.persistence.repository.BlogRepository
import com.github.jactor.persistence.repository.PersonRepository
import com.github.jactor.persistence.repository.UserRepository
import jakarta.persistence.EntityManager

/**
 * For å unngå å starte en spring-boot applikasjon og eventuelt laste spring-context på ny og når endringer som gjøres i
 * enhetstestene ikke skal ha innvirkning på spring-context<br>
 * Hvis test krever data i database, må det settes opp manuelt
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
internal abstract class AbstractSpringBootNoDirtyContextTest {

    @Autowired
    protected lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    protected lateinit var blogRepository: BlogRepository

    @Autowired
    protected lateinit var entityManager: EntityManager

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var personRepository: PersonRepository

    @Autowired
    protected lateinit var userRepository: UserRepository

    @LocalServerPort
    protected val port = 0
}
