package com.github.jactor.persistence.command

import com.github.jactor.persistence.dto.AddressDto
import com.github.jactor.persistence.dto.PersonDto
import com.github.jactor.persistence.dto.UserDto

data class CreateUserCommand(
        var username: String = "",
        var surname: String = "",
        var emailAddress: String? = null,
        var description: String? = null,
        var firstName: String? = null,
        var language: String? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var addressLine3: String? = null,
        var zipCode: String? = null,
        var city: String? = null,
        var coutnry: String? = null
) {
    constructor(username: String, surname: String) : this(
            username, surname,
            null, null, null, null, null, null, null, null, null
    )

    fun fetchPersonDto(): PersonDto {
        return PersonDto(null,
                fetchAddressDto(),
                language,
                firstName,
                surname,
                description
        )
    }

    private fun fetchAddressDto(): AddressDto? {
        return if (zipCode == null) null else AddressDto(null, zipCode, addressLine1, addressLine2, addressLine3, city, coutnry)
    }

    fun fetchUserDto(): UserDto {
        return UserDto(null, null, emailAddress, username)
    }
}
