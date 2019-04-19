package com.github.jactor.web

import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.servlet.http.HttpServletRequest

data class RequestManager(
        val contextPath: String,
        val httpServletRequest: HttpServletRequest,
        private var queryString: String? = null,
        private var languageParameter: String = ""
) {
    constructor(contextPath: String, httpServletRequest: HttpServletRequest) : this(
            contextPath, httpServletRequest, httpServletRequest.queryString, ""
    )

    fun fetchChosenView(): String {
        val requestURI = fetchWithoutContextPath()
        return if (requestURI[0] == '/') requestURI.substring(1) else requestURI
    }

    fun fetchCurrentUrl(): String {
        val requestURI = fetchWithoutContextPath()

        if (queryString == null || queryString!!.isBlank()) {
            return requestURI
        }

        val parametersWithoutLanguage = Arrays.stream<String>(queryString!!.split("&".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray())
                .filter { param -> !param.startsWith("lang=") }
                .collect(Collectors.toList<String>())

        if (parametersWithoutLanguage.isEmpty()) {
            return requestURI
        }

        return "$requestURI?" + parametersWithoutLanguage.joinToString("&")
    }

    private fun fetchWithoutContextPath() = httpServletRequest.getRequestURI().replace(contextPath.toRegex(), "")

    fun noLanguageParameters(): Boolean {
        if (queryString == null) {
            return false
        }

        languageParameter = Stream.of(*queryString!!.split("&".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray())
                .filter { string -> string.startsWith("lang=") }
                .findFirst().orElse("")

        return languageParameter.isEmpty()
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
