package com.github.jactor.web.dto

import java.time.LocalDateTime

data class AddressDto(
        var zipCode: String? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var addressLine3: String? = null,
        var city: String? = null,
        var country: String? = null
) : PersistenceDto(null, "todo", LocalDateTime.now(), "todo", LocalDateTime.now())

data class PersonDto(
        var address: AddressDto? = null,
        var locale: String? = null,
        var firstName: String? = null,
        var surname: String? = null,
        var description: String? = null
) : PersistenceDto(null, "todo", LocalDateTime.now(), "todo", LocalDateTime.now())

data class UserDto(
        var person: PersonDto? = null,
        var emailAddress: String? = null,
        var username: String? = null,
        var userType: UserType = UserType.ACTIVE
) : PersistenceDto(null, "todo", LocalDateTime.now(), "todo", LocalDateTime.now())

enum class UserType {
    ADMIN, ACTIVE, INACTIVE
}

sealed class PersistenceDto(
        var id: Long? = null,
        var createdBy: String = "todo",
        var timeOfCreation: LocalDateTime = LocalDateTime.now(),
        var modifiedBy: String = "todo",
        var timeOfModification: LocalDateTime = LocalDateTime.now()
)
