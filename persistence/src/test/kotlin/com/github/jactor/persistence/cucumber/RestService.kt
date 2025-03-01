package com.github.jactor.persistence.cucumber

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.util.UriComponentsBuilder

internal data class RestService(val baseUrl: String, var endpoint: String = "") {

    fun exchangeGet(
        parameternavn: String?,
        parameter: String?,
        restTemplate: () -> TestRestTemplate
    ): ResponseEntity<String> = initUrl(parameternavn, parameter).let { fullUrl ->
        restTemplate.invoke().exchange(fullUrl, HttpMethod.GET, null as HttpEntity<*>?, String::class.java)
    }

    fun exchangePost(json: String, restTemplate: () -> TestRestTemplate): ResponseEntity<String> {
        val fullUrl = initUrl()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        return try {
            restTemplate.invoke().exchange(fullUrl, HttpMethod.POST, HttpEntity(json, headers), String::class.java)
        } catch (e: HttpClientErrorException) {
            ResponseEntity(e.statusCode)
        }
    }

    private fun initUrl() = initUrl(null, null)
    private fun initUrl(navn: String?, parameter: String?) = navn?.let { initUrlWithBuilder(it, parameter) }
        ?: "$baseUrl$endpoint"

    private fun initUrlWithBuilder(navn: String, parameter: String?) =
        UriComponentsBuilder.fromHttpUrl("$baseUrl$endpoint")
            .queryParam(navn, parameter)
            .build()
            .toUriString()
}
