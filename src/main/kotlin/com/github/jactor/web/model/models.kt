package com.github.jactor.web.model

import com.github.jactor.web.Technology
import com.github.jactor.web.dto.AddressDto
import com.github.jactor.web.dto.PersonDto
import com.github.jactor.web.dto.UserDto
import java.util.Arrays.stream
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

data class HomePageModel(
        val paragraphs: List<String> = ArrayList(),
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

data class CurrentUrlManager(
        val contextPath: String,
        val httpServletRequest: HttpServletRequest
) {
    fun fetchChosenView(): String {
        val requestURI = fetchWithoutContextPath()
        return if (requestURI[0] == '/') requestURI.substring(1) else requestURI
    }

    fun fetch(): String {
        val requestURI = fetchWithoutContextPath()
        val queryString = httpServletRequest.queryString

        if (queryString == null || queryString.isBlank()) {
            return requestURI
        }

        val parametersWithoutLanguage = stream<String>(queryString.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .filter { param -> !param.startsWith("lang=") }
                .collect(Collectors.toList<String>())

        if (parametersWithoutLanguage.isEmpty()) {
            return requestURI
        }

        return "$requestURI?" + parametersWithoutLanguage.joinToString("&")
    }

    private fun fetchWithoutContextPath(): String {
        return httpServletRequest.getRequestURI().replace(contextPath.toRegex(), "")
    }
}
