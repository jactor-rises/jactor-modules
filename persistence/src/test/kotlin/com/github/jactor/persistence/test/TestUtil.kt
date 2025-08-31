package com.github.jactor.persistence.test

import java.util.UUID
import com.github.jactor.persistence.AddressEntity
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.GuestBookEntity
import com.github.jactor.persistence.GuestBookEntryEntity
import com.github.jactor.persistence.PersonEntity
import com.github.jactor.persistence.Person
import com.github.jactor.persistence.User
import com.github.jactor.persistence.UserEntity
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.Persistent

fun timestamped(username: String): String {
    return "$username@${java.lang.Long.toHexString(System.currentTimeMillis())}"
}

fun initUserEntity(
    id: UUID? = UUID.randomUUID(),
    person: PersonEntity = initPersonEntity()
) = UserEntity().apply {
    this.id = id
    this.person = person
    this.persistentDataEmbeddable = PersistentDataEmbeddable()
}

fun initPersonEntity(
    id: UUID? = UUID.randomUUID(),
    address: AddressEntity? = initAddressEntity()
) = PersonEntity().apply {
    this.id = id
    addressEntity = address
    this.persistentDataEmbeddable = PersistentDataEmbeddable()
}

fun initGuestBookEntity(id: UUID? = null) = GuestBookEntity().apply {
    this.id = id
    this.persistentDataEmbeddable = PersistentDataEmbeddable()
}

fun initGuestBookEntryEntity(
    id: UUID? = null,
    guestBook: GuestBookEntity = initGuestBookEntity()
) = GuestBookEntryEntity().apply {
    this.id = id
    this.persistentDataEmbeddable = PersistentDataEmbeddable()
    this.guestBook = guestBook
}

fun initAddressEntity(id: UUID? = UUID.randomUUID()) = AddressEntity().apply {
    this.id = id
    this.persistentDataEmbeddable = PersistentDataEmbeddable()
}

fun initPerson(
    address: Address? = null,
    firstName: String? = null,
    description: String? = null,
    locale: String? = null,
    persistent: Persistent = Persistent(),
    surname: String = "Doe",
) = Person(
    address = address,
    firstName = firstName,
    description = description,
    persistent = persistent,
    locale = locale,
    surname = surname,
)

fun initUser(
    persistent: Persistent = Persistent(),
    emailAddress: String? = null,
    person: Person? = null,
    username: String? = null,
    usertype: User.Usertype = User.Usertype.ACTIVE,
) = User(
    persistent = persistent,
    person = person,
    emailAddress = emailAddress,
    username = username,
    usertype = usertype,
)
