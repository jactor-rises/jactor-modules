package com.github.jactor.rises.persistence.cucumber

import io.cucumber.spring.ScenarioScope
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.EntityExchangeResult

@Component
@ScenarioScope
internal class ScenarioValues {
    lateinit var entityExchangeResult: EntityExchangeResult<String>
    lateinit var restService: RestService

    fun hentStatusKode(): HttpStatusCode = entityExchangeResult.status
    fun hentResponse(): String? = entityExchangeResult.responseBody
}
