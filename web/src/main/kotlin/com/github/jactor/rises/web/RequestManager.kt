package com.github.jactor.rises.web

import jakarta.servlet.http.HttpServletRequest
import java.util.Locale

data class RequestManager(
    val contextPath: String,
    val httpServletRequest: HttpServletRequest,
    private var queryString: String? = null,
    private var languageParameter: String = ""
) {
    constructor(contextPath: String, httpServletRequest: HttpServletRequest) : this(
        contextPath, httpServletRequest, httpServletRequest.queryString, languageParameter = ""
    )

    fun fetchChosenView(): String {
        val requestURI = fetchWithoutContextPath()

        if (requestURI.length == 1 || requestURI.isBlank()) return "home"

        return if (requestURI[0] == '/') requestURI.substring(1) else requestURI
    }

    fun fetchCurrentUrl(): String {
        val requestURI = fetchWithoutContextPath()

        if (queryString == null || queryString!!.isBlank()) {
            return requestURI
        }

        val parametersWithoutLanguage =
            queryString!!.split("&".toRegex()).dropLastWhile { it.isBlank() }
                .filter { param -> !param.startsWith("lang=") }

        if (parametersWithoutLanguage.isEmpty()) {
            return requestURI
        }

        return "$requestURI?${parametersWithoutLanguage.joinToString("&")}"
    }

    private fun fetchWithoutContextPath() = httpServletRequest.requestURI.replace(contextPath.toRegex(), "")

    fun noLanguageParameters(): Boolean {
        if (queryString == null) {
            return true
        }

        languageParameter = queryString!!
            .split("&".toRegex())
            .dropLastWhile { it.isBlank() }
            .firstOrNull { string -> string.startsWith("lang=") } ?: ""

        return languageParameter.isBlank()
    }

    fun fetchFrom(supportedLanguages: List<Language>, locale: Locale, defaultLanguage: Language): Language {
        return supportedLanguages.stream()
            .filter { language -> language.matches(locale) }
            .findFirst()
            .orElse(defaultLanguage)
    }

    fun fetchFromParameters(supportedLanguages: List<Language>, defaultLanguage: Language): Language {
        return supportedLanguages.stream()
            .filter { language -> language.matches(languageParameter) }
            .findFirst()
            .orElse(defaultLanguage)
    }
}
