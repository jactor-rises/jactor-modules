package com.github.jactor.persistence.command

import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.shared.dto.CreateUserCommandDto
import javax.validation.constraints.NotNull

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

    constructor(createUserCommand: @NotNull CreateUserCommandDto) : this(
            username = createUserCommand.username,
            surname = createUserCommand.surname,
            emailAddress = createUserCommand.emailAddress,
            description = createUserCommand.description,
            firstName = createUserCommand.firstName,
            language = createUserCommand.language,
            addressLine3 = createUserCommand.addressLine3,
            addressLine2 = createUserCommand.addressLine2,
            addressLine1 = createUserCommand.addressLine3,
            zipCode = createUserCommand.zipCode,
            city = createUserCommand.city,
            coutnry = createUserCommand.contry
    )

    fun fetchUserDto() = UserInternalDto(null, null, emailAddress, username)

    fun fetchPersonDto() = PersonInternalDto(null,
            fetchAddressDto(),
            language,
            firstName,
            surname,
            description
    )

    private fun fetchAddressDto() = if (zipCode == null) null else AddressInternalDto(null, zipCode, addressLine1, addressLine2, addressLine3, city, coutnry)
}

data class CreateUserCommandResponse(
        var userInternal: UserInternalDto = UserInternalDto()
)
