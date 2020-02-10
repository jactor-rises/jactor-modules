package com.github.jactor.persistence.command

import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto

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

    fun fetchPersonDto(): PersonInternalDto {
        return PersonInternalDto(null,
                fetchAddressDto(),
                language,
                firstName,
                surname,
                description
        )
    }

    private fun fetchAddressDto(): AddressInternalDto? {
        return if (zipCode == null) null else AddressInternalDto(null, zipCode, addressLine1, addressLine2, addressLine3, city, coutnry)
    }

    fun fetchUserDto(): UserInternalDto {
        return UserInternalDto(null, null, emailAddress, username)
    }
}

data class CreateUserCommandResponse (
        var userInternal: UserInternalDto = UserInternalDto()
)
