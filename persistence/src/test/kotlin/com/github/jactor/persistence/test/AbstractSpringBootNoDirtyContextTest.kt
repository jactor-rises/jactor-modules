package com.github.jactor.persistence.test

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

/**
 * For å unngå å starte en spring-boot applikasjon og eventuelt laste spring-context på ny og når endringer som gjøres i
 * enhetstestene ikke skal ha innvirkning på spring-context<br>
 * Hvis test krever data i database, må det settes opp manuelt
 */
@SpringBootTest
@Transactional
abstract class AbstractSpringBootNoDirtyContextTest