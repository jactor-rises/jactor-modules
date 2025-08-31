package com.github.jactor.persistence

import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.PersistentData
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto

fun PersistentData.harIdentifikator(): Boolean = persistentDto.id != null
fun PersistentData.harIkkeIdentifikator(): Boolean = persistentDto.id == null
fun PersonDto.toModel() = Person(
    persistent = Persistent(persistentDto = persistentDto),
    address = address?.let { Address(addressDto = it) },
    locale = locale,
    firstName = firstName,
    surname = surname,
    description = description
)

fun UserDto.toModel() = User(
    persistent = Persistent(persistentDto = persistentDto),
    person = person?.let { Person(personDto = it) },
    emailAddress = emailAddress,
    username = username,
    usertype = User.Usertype.valueOf(userType.name)
)
