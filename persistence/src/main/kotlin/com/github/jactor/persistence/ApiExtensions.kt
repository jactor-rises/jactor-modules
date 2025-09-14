package com.github.jactor.persistence

import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto
import java.time.LocalDateTime

fun AddressDto.toAddress() = Address(
    persistent = Persistent(persistentDto = persistentDto),

    addressLine1 = requireNotNull(addressLine1) { "Address line 1 cannot be null!" },
    addressLine2 = addressLine2,
    addressLine3 = addressLine3,
    city = requireNotNull(city) { "City cannot be null!" },
    country = country,
    zipCode = requireNotNull(zipCode) { "Zip code cannot be null!" },
)

fun PersistentDto.toPersistent() = Persistent(
    id = id,
    createdBy = requireNotNull(createdBy) { "Created by cannot be null!" },
    timeOfCreation = timeOfCreation ?: LocalDateTime.now(),
    modifiedBy = requireNotNull(modifiedBy) { "Modified by cannot be null!" },
    timeOfModification = timeOfModification ?: LocalDateTime.now()
)

fun PersonDto.toPerson() = Person(
    persistent = Persistent(persistentDto = persistentDto),
    address = address?.toAddress(),
    locale = locale,
    firstName = firstName,
    surname = surname,
    description = description
)

fun UserDto.toUser() = User(
    persistent = persistentDto.toPersistent(),
    person = person?.let { Person(personDto = it) },
    emailAddress = emailAddress,
    username = username,
    usertype = User.Usertype.valueOf(userType.name)
)
