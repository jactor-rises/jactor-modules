package com.github.jactor.cucumber.persistence

import com.github.jactor.cucumber.ScenarioValues.Companion.responseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.HttpURLConnection
import java.net.URL

data class Application(val contextUrl: String, var completeUrl: String?) {
    constructor(contextUrl: String) : this(contextUrl, null)

    fun doGet() {
        val url = URL("$contextUrl${if (completeUrl != null) completeUrl else ""}")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        responseEntity = ResponseEntity(HttpStatus.valueOf(connection.responseCode))
    }
}
