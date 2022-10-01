package com.github.jactor.cucumber

import com.github.jactor.cucumber.persistence.Application
import org.springframework.http.ResponseEntity

class ScenarioValues {
    companion object {
        lateinit var application: Application
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
