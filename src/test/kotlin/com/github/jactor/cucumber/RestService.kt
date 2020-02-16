package com.github.jactor.cucumber

import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

data class RestService(val baseUrl: String, var url: String = "") {
    private lateinit var responseEntity: ResponseEntity<String>
    private var restTemplate: RestTemplate? = null

    fun exchangeGet() {
        exchangeGet(null, null)
    }

    fun exchangeGet(navn: String?, parameter: String?) {
        if (restTemplate == null) {
            restTemplate = RestTemplate()
        }

        val fullUrl =
                if (navn != null) UriComponentsBuilder.fromHttpUrl(baseUrl + url)
                        .queryParam(navn, parameter).build().toUriString()
                else baseUrl + url

        responseEntity = restTemplate!!.exchange(fullUrl, HttpMethod.GET, null, String::class.java)
    }

    fun hentStatusKode() = responseEntity.statusCode
    fun hentResponse() = responseEntity.body
}
