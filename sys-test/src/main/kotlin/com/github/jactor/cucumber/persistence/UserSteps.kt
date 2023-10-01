 package com.github.jactor.cucumber.persistence

import com.github.jactor.cucumber.RestService
import com.github.jactor.cucumber.ScenarioValues.Companion.restService
import com.github.jactor.cucumber.ScenarioValues.Companion.uniqueKey
import com.github.jactor.cucumber.UniqueKey
import assertk.assertThat
import assertk.assertions.startsWith
import io.cucumber.java8.No

 @Suppress("unused") // brukes av cucumber
class UserSteps : No {
    init {
        Når("en post gjøres for unik nøkkel {string} med body:") { keyToBeUnique: String, body: String ->
            uniqueKey = UniqueKey(keyToBeUnique)
            restService.exchangePost(uniqueKey.useIn(body))
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
            restService.exchangePost(uniqueKey.useIn(body))
        }
    }
}
