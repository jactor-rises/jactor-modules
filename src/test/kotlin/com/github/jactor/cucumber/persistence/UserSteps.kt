package com.github.jactor.cucumber.persistence

import com.github.jactor.cucumber.RestService
import com.github.jactor.cucumber.StepValues.Companion.restService
import com.github.jactor.cucumber.StepValues.Companion.uniqueKey
import io.cucumber.java8.No
import org.assertj.core.api.Assertions.assertThat

class UserSteps : No {
    init {
        Når("en post gjøres for unik nøkkel {string} med body:") { keyToBeUnique: String, body: String ->
            uniqueKey = "$keyToBeUnique.${java.lang.Long.toHexString(System.currentTimeMillis())}"
            val jsonBody = body.replace(keyToBeUnique, uniqueKey)
            restService.exchangePost(jsonBody)
        }

        Og("gitt nøkkel {string} og url til resttjeneste: {string}") { key: String, url: String ->
            assertThat(uniqueKey).`as`("ujent \"unik\" nøkkel for skal brukes i url...").startsWith(key)
            val urlWithUniqueKey = url.replace(key, uniqueKey)
            restService = RestService(urlWithUniqueKey)
        }

        Og("url til resttjeneste med den unike nøkkelen: {string}") { key: String, url: String ->
            assertThat(uniqueKey).`as`("ujent \"unik\" nøkkel for skal brukes i url...").startsWith(key)
            val urlWithUniqueKey = url.replace(key, uniqueKey)
            restService = RestService(urlWithUniqueKey)
        }

        Når("en post gjøres for unik nøkkel {string}, men den unike nøkkelen gjenbrukes på body:") { key: String, body: String ->
            assertThat(uniqueKey).`as`("ujent \"unik\" nøkkel som skal gjenbrukes i json body...").startsWith(key)
            val jsonBody = body.replace(key, uniqueKey)
            restService.exchangePost(jsonBody)
        }
    }
}
