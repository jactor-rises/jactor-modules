package com.github.jactor.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityManager

/**
 * For å unngå å starte en spring-boot applikasjon og eventuelt laste spring-context på ny og når endringer som gjøres i
 * enhetstestene ikke skal ha innvirkning på spring-context<br>
 * Hvis test krever data i database, må det settes opp manuelt
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
abstract class AbstractSpringBootNoDirtyContextTest {
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
