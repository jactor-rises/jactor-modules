package com.github.jactor.persistence

import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.shared.api.PersistentData
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto

fun PersistentData.harIdentifikator(): Boolean = persistentDto.id != null
fun PersistentData.harIkkeIdentifikator(): Boolean = persistentDto.id == null
fun PersonDto.toModel() = PersonModel(
    persistentModel = PersistentModel(persistentDto = persistentDto),
    address = address?.let { AddressModel(addressDto = it) },
    locale = locale,
    firstName = firstName,
    surname = surname,
    description = description
)

fun UserDto.toModel() = UserModel(
    persistentModel = PersistentModel(persistentDto = persistentDto),
    person = person?.let { PersonModel(personDto = it) },
    emailAddress = emailAddress,
    username = username,
    usertype = UserModel.Usertype.valueOf(userType.name)
)
