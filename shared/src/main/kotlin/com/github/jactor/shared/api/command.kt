package com.github.jactor.shared.api

import java.util.UUID
import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
@Schema(description = "Metadata for creation of a user")
data class CreateUserCommand(
    @param:Schema(description = "The username of a user") val username: String = "",
    @param:Schema(description = "The surname of a user") val surname: String = "",
    @param:Schema(description = "The email address of a user") val emailAddress: String? = null,
    @param:Schema(description = "The description of a user") val description: String? = null,
    @param:Schema(description = "The first name of a user") val firstName: String? = null,
    @param:Schema(description = "The language of a user") val language: String? = null,
    @param:Schema(description = "The users first address line") val addressLine1: String? = null,
    @param:Schema(description = "The users second address line") val addressLine2: String? = null,
    @param:Schema(description = "The users third address line") val addressLine3: String? = null,
    @param:Schema(description = "The zip code of a user") val zipCode: String? = null,
    @param:Schema(description = "The city of a user") val city: String? = null,
    @param:Schema(description = "The country of a user") val country: String? = null
) {
    fun toUserDto() = UserDto(
        persistentDto = PersistentDto(),
        person = toPersonDto(),
        emailAddress = emailAddress,
        username = username
    )

    fun toPersonDto() = PersonDto(
        persistentDto = PersistentDto(),
        address = toAddressDto(),
        locale = language,
        firstName = firstName,
        surname = surname,
        description = description
    )

    private fun toAddressDto(): AddressDto? = zipCode?.let { AddressDto(
            persistentDto = PersistentDto(id = UUID.randomUUID()),
            zipCode = zipCode,
            addressLine1 = addressLine1,
            addressLine2 = addressLine2,
            addressLine3 = addressLine3,
            city = city,
            country = country
        )
    }
}
