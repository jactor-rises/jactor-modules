package com.github.jactor.persistence.command

import com.github.jactor.persistence.dto.AddressDto
import com.github.jactor.persistence.dto.PersonDto
import com.github.jactor.persistence.dto.UserDto

data class CreateUserCommand(
        var username: String,
        var surname: String,
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

    fun fetchPersonWithAddress(): PersonDto {
        return PersonDto(null,
                AddressDto(null, zipCode, addressLine1, addressLine2, addressLine3, city, coutnry),
                language,
                firstName,
                surname,
                description
        )
    }

    fun fetchUserDto(person: PersonDto): UserDto {
        return UserDto(null, person, emailAddress, username)
    }
}
