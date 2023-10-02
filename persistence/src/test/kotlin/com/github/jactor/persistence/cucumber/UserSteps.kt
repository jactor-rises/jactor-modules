package com.github.jactor.persistence.cucumber

import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.responseEntity
import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.restService
import io.cucumber.java8.No

@Suppress("unused", "LeakingThis") // bestemmes av cucumber
internal class UserSteps : No, PersistenceCucumberContextConfiguration() {
    init {
        Når("en post gjøres for body:") { body: String ->
            responseEntity = restService.exchangePost(body) { testRestTemplate }
        }

        Og("gitt url {string}") { url: String ->
            restService = RestService(url)
        }
    }
}
