package com.github.jactor.web.model

import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto
import com.github.jactor.web.Technology

@JvmRecord
data class HomePageModel(val technologies: List<Technology> = ArrayList())

@JvmRecord
data class UserModel(
    private val user: UserDto,
    private val person: PersonDto = user.person ?: PersonDto(),
    private val address: AddressDto = person.address ?: AddressDto()
) {
    constructor(user: UserDto) : this(user = user, person = user.person ?: PersonDto())

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
        val surname = person.surname
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
