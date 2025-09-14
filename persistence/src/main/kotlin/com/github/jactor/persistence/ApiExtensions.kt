package com.github.jactor.persistence

import java.time.LocalDateTime
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto

fun AddressDto.toAddress() = Address(
    persistent = persistentDto.toPersistent(),

    addressLine1 = requireNotNull(addressLine1) { "Address line 1 cannot be null!" },
    addressLine2 = addressLine2,
    addressLine3 = addressLine3,
    city = requireNotNull(city) { "City cannot be null!" },
    country = country,
    zipCode = requireNotNull(zipCode) { "Zip code cannot be null!" },
)

fun GuestBookDto.toGuestBook() = GuestBook(
    persistent = persistentDto.toPersistent(),
    entries = emptySet(),
    title = requireNotNull(title) { "Title cannot be null!" },
    user = requireNotNull(userDto?.toUser()) { "User cannot be null!" },
).let { parent -> parent.copy(entries = entries.map { it.toGuestBookEntry(parent = parent) }.toSet()) }

fun GuestBookEntryDto.toGuestBookEntry() = GuestBookEntry(
    persistent = persistentDto.toPersistent(),
    creatorName = requireNotNull(creatorName) { "Creator name cannot be null!" },
    entry = requireNotNull(entry) { "Entry cannot be null!" },
    guestBook = guestBook?.toGuestBook()
)

fun GuestBookEntryDto.toGuestBookEntry(parent: GuestBook?) = GuestBookEntry(
    persistent = persistentDto.toPersistent(),
    creatorName = requireNotNull(creatorName) { "Creator name cannot be null!" },
    entry = requireNotNull(entry) { "Entry cannot be null!" },
    guestBook = parent
)

fun PersistentDto.toPersistent() = Persistent(
    id = id,
    createdBy = requireNotNull(createdBy) { "Created by cannot be null!" },
    timeOfCreation = timeOfCreation ?: LocalDateTime.now(),
    modifiedBy = requireNotNull(modifiedBy) { "Modified by cannot be null!" },
    timeOfModification = timeOfModification ?: LocalDateTime.now()
)

fun PersonDto.toPerson() = Person(
    persistent = persistentDto.toPersistent(),
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
