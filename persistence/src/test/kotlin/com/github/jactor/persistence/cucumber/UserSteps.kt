package com.github.jactor.persistence.cucumber

import io.cucumber.java8.No

@Suppress("unused", "LeakingThis") // bestemmes av cucumber
internal class UserSteps : No, PersistenceCucumberContextConfiguration() {

    init {
        Når("en post gjøres for body:") { body: String ->
            scenarioValues.responseEntity = scenarioValues.restService.exchangePost(body) { testRestTemplate }
        }

        Og("gitt url {string}") { url: String ->
            scenarioValues.restService = RestService(url)
        }
    }
}
