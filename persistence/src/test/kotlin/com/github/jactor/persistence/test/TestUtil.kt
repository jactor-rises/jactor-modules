package com.github.jactor.persistence.test

import java.util.UUID
import com.github.jactor.persistence.address.AddressEntity
import com.github.jactor.persistence.guestbook.GuestBookEntity
import com.github.jactor.persistence.guestbook.GuestBookEntryEntity
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.entity.PersonEntity
import com.github.jactor.persistence.entity.UserEntity
import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

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

fun Assert<List<String>>.containsSubstring(expected: String) = given { strings ->
    strings.forEach {
        if (it.contains(expected)) {
            return@given
        }
    }

    expected("to contain substring:${show(expected)}, but list was:${show(strings)}")
}
