package com.github.jactor.persistence.cucumber

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

internal data class RestService(val baseUrl: String, var endpoint: String = "") {

    fun exchangeGet(
        parameternavn: String?,
        parameter: String?,
        webTestClient: () -> WebTestClient
    ): EntityExchangeResult<String> = initUrl(parameternavn, parameter).let {
        webTestClient.invoke()
            .get()
            .uri(it)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
    }

    fun exchangePost(json: String, webTestClient: () -> WebTestClient): EntityExchangeResult<String> = initUrl().let {
        webTestClient.invoke()
            .post()
            .uri(it)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(json)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(String::class.java)
            .returnResult()
    }

    private fun initUrl() = initUrl(null, null)
    private fun initUrl(navn: String?, parameter: String?) = navn?.let { initUrlWithBuilder(it, parameter) }
        ?: "$baseUrl$endpoint"

    private fun initUrlWithBuilder(navn: String, parameter: String?): String = UriComponentsBuilder
        .fromUriString("$baseUrl$endpoint")
        .queryParam(navn, parameter)
        .build()
        .toUriString()
}
