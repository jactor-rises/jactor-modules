package com.github.jactor.persistence.cucumber

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.annotation.Transactional
import com.github.jactor.persistence.UserRepository
import io.cucumber.spring.CucumberContextConfiguration

@Transactional
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
internal abstract class AbstractSpringBootCucumberConfiguration {
    internal val standardUsers = setOf("jactor", "tip")

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var scenarioValues: ScenarioValues
}
