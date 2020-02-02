package com.github.jactor.cucumber.persistence

import java.net.HttpURLConnection
import java.net.URL

data class Application(val contextUrl: String, var completeUrl: String?) {
    constructor(contextUrl: String) : this(contextUrl, null)

    fun hentResponseStatusForGet() : Int {
        val url = URL("$contextUrl${if (completeUrl != null) completeUrl else ""}")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        return connection.responseCode
    }
}
