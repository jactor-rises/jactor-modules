package com.github.jactor.persistence.api.command

import java.util.UUID
import com.github.jactor.persistence.dto.AddressModel
import com.github.jactor.persistence.dto.PersistentModel
import com.github.jactor.persistence.dto.PersonModel
import com.github.jactor.persistence.dto.UserModel
import com.github.jactor.shared.api.CreateUserCommandDto

@JvmRecord
data class CreateUserCommand(
    val username: String = "",
    val surname: String = "",
    val emailAddress: String? = null,
    val description: String? = null,
    val firstName: String? = null,
    val language: String? = null,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val addressLine3: String? = null,
    val zipCode: String? = null,
    val city: String? = null,
    val coutnry: String? = null
) {
    constructor(createUserCommand: CreateUserCommandDto) : this(
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

    fun fetchUserDto() = UserModel(
        persistentModel = PersistentModel(),
        personInternal = null,
        emailAddress = emailAddress,
        username = username
    )

    fun fetchPersonDto() = PersonModel(
        persistentModel = PersistentModel(),
        address = fetchAddressDto(),
        locale = language,
        firstName = firstName,
        surname = surname,
        description = description
    )

    private fun fetchAddressDto(): AddressModel? {
        return if (zipCode == null) null else AddressModel(
            persistentModel = PersistentModel(id = UUID.randomUUID()),
            zipCode = zipCode,
            addressLine1 = addressLine1,
            addressLine2 = addressLine2,
            addressLine3 = addressLine3,
            city = city,
            country = coutnry
        )
    }
}

data class CreateUserCommandResponse(
    val userInternal: UserModel = UserModel()
)
