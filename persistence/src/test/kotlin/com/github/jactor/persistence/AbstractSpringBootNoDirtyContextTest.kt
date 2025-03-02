package com.github.jactor.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jactor.persistence.address.AddressRepository
import com.github.jactor.persistence.blog.BlogEntryRepository
import com.github.jactor.persistence.blog.BlogRepository
import com.github.jactor.persistence.blog.BlogService
import com.github.jactor.persistence.guestbook.GuestBookEntryRepository
import com.github.jactor.persistence.guestbook.GuestBookRepository
import com.github.jactor.persistence.guestbook.GuestBookService
import com.github.jactor.persistence.person.PersonRepository
import com.github.jactor.persistence.user.UserRepository
import com.ninjasquad.springmockk.SpykBean
import jakarta.persistence.EntityManager

/**
 * For å unngå å starte en spring-boot applikasjon og eventuelt laste spring-context på ny og når endringer som gjøres i
 * enhetstestene ikke skal ha innvirkning på spring-context<br>
 * Hvis test krever data i database, må det settes opp manuelt
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
abstract class AbstractSpringBootNoDirtyContextTest {

    @Autowired
    protected lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    protected lateinit var addressRepository: AddressRepository

    @Autowired
    protected lateinit var blogRepository: BlogRepository

    @Autowired
    protected lateinit var blogEntryRepository: BlogEntryRepository

    @Autowired
    protected lateinit var guestBookRepository: GuestBookRepository

    @Autowired
    protected lateinit var guestBookEntryRepository: GuestBookEntryRepository

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var personRepository: PersonRepository

    @Autowired
    protected lateinit var userRepository: UserRepository

    @SpykBean
    protected lateinit var blogServiceSpyk: BlogService

    @SpykBean
    protected lateinit var guestBookServiceSpyk: GuestBookService

    @SpykBean
    protected lateinit var personRepositorySpyk: PersonRepository

    @SpykBean
    protected lateinit var userRepositorySpyk: UserRepository

    @LocalServerPort
    protected val port = 0

    @Value("\${server.servlet.context-path}")
    protected lateinit var contextPath: String

    // database operations

    @Autowired
    private lateinit var entityManager: EntityManager

    protected fun <T> flush(databaseOperation: () -> T): T {
        val entity = databaseOperation.invoke()
        entityManager.flush()
        entityManager.clear()

        return entity
    }

    protected fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }
}
