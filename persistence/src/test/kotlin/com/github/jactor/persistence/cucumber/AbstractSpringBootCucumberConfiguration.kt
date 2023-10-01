package com.github.jactor.persistence.cucumber

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.transaction.annotation.Transactional
import com.github.jactor.persistence.repository.UserRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
internal abstract class AbstractSpringBootCucumberConfiguration {
    internal val standardUsers = setOf("jactor", "tip")

    @Autowired
    protected lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    protected lateinit var userRepository: UserRepository

}
