package com.github.jactor.persistence.cucumber

import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.EntityExchangeResult
import io.cucumber.spring.ScenarioScope

@Component
@ScenarioScope
internal class ScenarioValues {
    lateinit var entityExchangeResult: EntityExchangeResult<String>
    lateinit var restService: RestService

    fun hentStatusKode(): HttpStatusCode = entityExchangeResult.status
    fun hentResponse(): String? = entityExchangeResult.responseBody
}
