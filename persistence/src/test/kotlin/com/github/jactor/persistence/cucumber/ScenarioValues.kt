package com.github.jactor.persistence.cucumber

import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import io.cucumber.spring.ScenarioScope

@Component
@ScenarioScope
internal class ScenarioValues {
    lateinit var responseEntity: ResponseEntity<String>
    lateinit var restService: RestService

    fun hentStatusKode(): HttpStatusCode = responseEntity.statusCode
    fun hentResponse(): String? = responseEntity.body
}
