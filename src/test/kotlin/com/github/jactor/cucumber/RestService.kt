package com.github.jactor.cucumber

import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

data class RestService(val baseUrl: String, var url: String = "") {
    private lateinit var responseEntity: ResponseEntity<String>
    private var restTemplate: RestTemplate? = null

    fun exchangeGet() {
        if (restTemplate == null) {
            restTemplate = RestTemplate()
        }

        responseEntity = restTemplate!!.exchange(baseUrl + url, HttpMethod.GET, null, String::class.java)
    }

    fun hentStatusKode() = responseEntity.statusCode
    fun hentResponse() = responseEntity.body
}
