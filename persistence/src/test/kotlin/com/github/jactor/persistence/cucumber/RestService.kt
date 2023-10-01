package com.github.jactor.persistence.cucumber

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.util.UriComponentsBuilder
import com.github.jactor.persistence.cucumber.ScenarioValues.Companion.responseEntity

internal data class RestService(val baseUrl: String, var url: String = "") {
    private val restTemplate = TestRestTemplate()

    fun exchangeGet() {
        exchangeGet(null, null)
    }

    fun exchangeGet(navn: String?, parameter: String?) {
        val fullUrl = initUrl(navn, parameter)

        responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, null, String::class.java)
    }

    fun exchangePost(json: String) {
        val fullUrl = initUrl()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        responseEntity = try {
            restTemplate.exchange(fullUrl, HttpMethod.POST, HttpEntity(json, headers), String::class.java)
        } catch (e: HttpClientErrorException) {
            ResponseEntity(e.statusCode)
        }
    }

    private fun initUrl() = initUrl(null, null)
    private fun initUrl(navn: String?, parameter: String?) = if (navn != null) initUrlWithBuilder(navn, parameter) else baseUrl + url
    private fun initUrlWithBuilder(navn: String, parameter: String?) = UriComponentsBuilder.fromHttpUrl(baseUrl + url)
        .queryParam(navn, parameter)
        .build()
        .toUriString()
}
