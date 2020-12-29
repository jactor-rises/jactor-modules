package com.github.jactor.cucumber

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

data class RestService(val baseUrl: String, var url: String = "") {
    private var httpClientErrorException: HttpClientErrorException? = null
    private var initilazed: Boolean = false
    private var responseEntity: ResponseEntity<String>? = null
    private lateinit var restTemplate: RestTemplate

    fun exchangeGet() {
        exchangeGet(null, null)
    }

    fun exchangeGet(navn: String?, parameter: String?) {
        initRestTemplate()
        val fullUrl = initUrl(navn, parameter)

        responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, null, String::class.java)
    }

    fun exchangePost(json: String) {
        initRestTemplate()
        val fullUrl = initUrl()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        try {
            responseEntity = restTemplate.exchange(fullUrl, HttpMethod.POST, HttpEntity(json, headers), String::class.java)
        } catch (e: HttpClientErrorException) {
            httpClientErrorException = e
        }
    }

    private fun initUrl() = initUrl(null, null)
    private fun initUrl(navn: String?, parameter: String?) = if (navn != null) initUrlWithBuilder(navn, parameter) else baseUrl + url
    private fun initUrlWithBuilder(navn: String, parameter: String?) = UriComponentsBuilder.fromHttpUrl(baseUrl + url)
        .queryParam(navn, parameter)
        .build()
        .toUriString()

    private fun initRestTemplate() {
        if (!initilazed) {
            restTemplate = RestTemplate()
            initilazed = true
        }
    }

    fun hentStatusKode(): HttpStatus {
        if (responseEntity != null) return responseEntity!!.statusCode
        if (httpClientErrorException != null) return httpClientErrorException!!.statusCode

        return HttpStatus.I_AM_A_TEAPOT
    }

    fun hentResponse() = responseEntity?.body
}
