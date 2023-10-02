package com.github.jactor.persistence.cucumber

import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.responseEntity
import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.restService
import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.uniqueKey
import assertk.assertThat
import assertk.assertions.startsWith
import io.cucumber.java8.No

@Suppress("unused", "LeakingThis") // bestemmes av cucumber
internal class UserSteps : No, PersistenceCucumberContextConfiguration() {
    init {
        Når("en post gjøres for unik nøkkel {string} med body:") { keyToBeUnique: String, body: String ->
            uniqueKey = UniqueKey(keyToBeUnique)
            responseEntity = restService.exchangePost(uniqueKey.useIn(body)) { testRestTemplate }
        }

        Og("gitt nøkkel {string} og base url {string}") { key: String, url: String ->
            assertThat(uniqueKey.fetchUniqueKey()).startsWith(key)
            restService = RestService(uniqueKey.useIn(url))
        }

        Og("url til resttjeneste med den unike nøkkelen: {string}") { key: String, url: String ->
            assertThat(uniqueKey.fetchUniqueKey()).startsWith(key)
            restService = RestService(uniqueKey.useIn(url))
        }

        Når("en post gjøres for unik nøkkel {string}, men den unike nøkkelen gjenbrukes på body:") { key: String, body: String ->
            assertThat(uniqueKey.fetchUniqueKey()).startsWith(key)
            responseEntity = restService.exchangePost(uniqueKey.useIn(body)) { testRestTemplate }
        }
    }
}
