package com.github.jactor.web.model

import com.github.jactor.shared.dto.AddressDto
import com.github.jactor.shared.dto.PersonDto
import com.github.jactor.shared.dto.UserDto
import com.github.jactor.web.Technology

data class HomePageModel(val technologies: List<Technology> = ArrayList())
data class UserModel(private val user: UserDto) {

    private val address: AddressDto
    private val person: PersonDto = if (user.person != null) user.person!! else PersonDto()

    init {
        address = if (person.address != null) person.address!! else AddressDto()
    }

    fun fetchAddress(): List<String> {
        val allFields = listOf(
            address.addressLine1,
            address.addressLine2,
            address.addressLine3,
            address.zipCode,
            address.city
        )

        val fieldsNotNull = mutableListOf<String>()

        allFields.forEach {
            if (it != null) {
                fieldsNotNull.add(it)
            }
        }

        return fieldsNotNull
    }

    fun fetchFullName(): String {
        val surname = person.surname ?: ""
        val firstName = person.firstName ?: ""

        val fullName = "$firstName $surname".trim()

        return fullName.ifEmpty { throw IllegalStateException("Unable to determine name of person") }
    }

    fun fetchUsername(): String? {
        return user.username
    }

    fun fetchDescriptionCode(): String? {
        return person.description
    }
}
