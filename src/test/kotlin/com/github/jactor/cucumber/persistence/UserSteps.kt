package com.github.jactor.cucumber.persistence

import com.github.jactor.cucumber.RestServiceSteps
import io.cucumber.java.no.Når
import io.cucumber.java.no.Og
import org.assertj.core.api.Assertions.assertThat


class UserSteps {
    companion object {
        private lateinit var uniqueKey: String
    }

    @Når("en post gjøres for unik nøkkel {string} med body:")
    fun `en post gjores for unik nokkel med body`(keyToBeUnique: String, body: String) {
        uniqueKey = "$keyToBeUnique.${java.lang.Long.toHexString(System.currentTimeMillis())}"
        val jsonBody = body.replace(keyToBeUnique, uniqueKey)
        RestServiceSteps().`en post gjores med body`(jsonBody)
    }

    @Og("gitt nøkkel {string} og url til resttjeneste: {string}")
    fun `gitt nokkel og url til resttjeneste`(key: String, url: String) {
        assertThat(uniqueKey).`as`("ujent \"unik\" nøkkel for skal brukes i url...").startsWith(key)
        val urlWithUniqueKey = url.replace(key, uniqueKey)
        RestServiceSteps().`url til resttjeneste`(urlWithUniqueKey)
    }

    @Og("url til resttjeneste med den unike nøkkelen: {string}")
    fun `url til resttjeneste med den unike nokkelen`(key: String, url: String) {
        assertThat(uniqueKey).`as`("ujent \"unik\" nøkkel for skal brukes i url...").startsWith(key)
        val urlWithUniqueKey = url.replace(key, uniqueKey)
        RestServiceSteps().`url til resttjeneste`(urlWithUniqueKey)
    }

    @Når("en post gjøres for unik nøkkel {string}, men den unike nøkkelen gjenbrukes på body:")
    fun `en post gjores og den unike nokkelen gjenbrukes pa body`(key: String, body: String) {
        assertThat(uniqueKey).`as`("ujent \"unik\" nøkkel som skal gjenbrukes i json body...").startsWith(key)
        val jsonBody = body.replace(key, uniqueKey)
        RestServiceSteps().`en post gjores med body`(jsonBody)
    }
}
