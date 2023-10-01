package com.github.jactor.persistence.cucumber

import org.springframework.http.ResponseEntity

internal class ScenarioValues {
    companion object {
        lateinit var responseEntity: ResponseEntity<String>
        lateinit var restService: RestService
        lateinit var uniqueKey: UniqueKey

        fun hentStatusKode() = responseEntity.statusCode
        fun hentResponse() = responseEntity.body
    }
}

data class UniqueKey(val key: String, private val suffix: String = java.lang.Long.toHexString(System.currentTimeMillis())) {
    fun fetchUniqueKey() = "$key.$suffix"
    fun useIn(string: String) = string.replace(key, fetchUniqueKey())
}
