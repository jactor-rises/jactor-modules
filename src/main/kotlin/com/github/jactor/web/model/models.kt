package com.github.jactor.web.model

import com.github.jactor.web.Technology
import com.github.jactor.web.dto.AddressDto
import com.github.jactor.web.dto.PersonDto
import com.github.jactor.web.dto.UserDto
import java.util.Arrays.stream
import java.util.Locale
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList

data class HomePageModel(
        val technologies: List<Technology> = ArrayList()
)

data class UserModel(private val user: UserDto) {

    private val address: AddressDto
    private val person: PersonDto = if (user.person != null) user.person!! else PersonDto()

    init {
        address = if (person.address != null) person.address!! else AddressDto()
    }

    fun fetchAddress(): List<String> {
        val address = ArrayList<String>()
        val addressLine1 = fetchAddressLine1()
        val addressLine2 = fetchAddressLine2()
        val addressLine3 = fetchAddressLine3()
        val city = fetchCity()
        val zipCode = fetchZipCode()

        if (addressLine1 != null) address.add(addressLine1)
        if (addressLine2 != null) address.add(addressLine2)
        if (addressLine3 != null) address.add(addressLine3)
        if (zipCode != null) address.add(zipCode)
        if (city != null) address.add(city)

        return address
    }

    fun fetchFullName(): String {
        val surname = fetchSurname()
        val firstName = fetchFirstname()

        val fullName = "$firstName $surname".trim()

        return if (fullName.isNotEmpty()) fullName else throw IllegalStateException("Unable to determine name of person")
    }

    private fun fetchAddressLine1(): String? {
        return address.addressLine1
    }

    private fun fetchAddressLine2(): String? {
        return address.addressLine2
    }

    private fun fetchAddressLine3(): String? {
        return address.addressLine3
    }

    private fun fetchCity(): String? {
        return address.city
    }

    private fun fetchZipCode(): String? {
        return address.zipCode
    }

    private fun fetchFirstname(): String {
        return if (person.firstName != null) person.firstName!! else ""
    }

    private fun fetchSurname(): String {
        return if (person.surname != null) person.surname!! else ""
    }

    fun fetchUsername(): String? {
        return user.username
    }

    fun fetchDescriptionCode(): String? {
        return person.description
    }
}

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

        val parametersWithoutLanguage = stream<String>(queryString!!.split("&".toRegex())
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
                .orElse(defaultLanguage);

    }

    fun fetchFromParameters(supportedLanguages: List<Language>, defaultLanguage: Language): Language {
        return supportedLanguages.stream()
                .filter { language -> language.matches(languageParameter) }
                .findFirst()
                .orElse(defaultLanguage);
    }
}

data class Language(
        val locale: Locale,
        val name: String = ""
) {
    constructor(locale: Locale) : this(locale, "")

    fun matches(locale: Locale): Boolean {
        return this.locale == locale
    }

    fun matches(language: String): Boolean {
        return if (language.startsWith("lang=")) locale.language == language.replace("lang=", "")
        else locale.language == language
    }
}