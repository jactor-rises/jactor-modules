package com.github.jactor.persistence.test

import java.util.UUID
import com.github.jactor.persistence.AddressEntity
import com.github.jactor.persistence.AddressModel
import com.github.jactor.persistence.GuestBookEntity
import com.github.jactor.persistence.GuestBookEntryEntity
import com.github.jactor.persistence.PersonEntity
import com.github.jactor.persistence.PersonModel
import com.github.jactor.persistence.UserEntity
import com.github.jactor.persistence.UserModel
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.PersistentModel

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

fun initPersonModel(
    address: AddressModel? = null,
    firstName: String? = null,
    description: String? = null,
    locale: String? = null,
    persistentModel: PersistentModel = PersistentModel(),
    surname: String = "Doe",
) = PersonModel(
    address = address,
    firstName = firstName,
    description = description,
    persistentModel = persistentModel,
    locale = locale,
    surname = surname,
)

fun initUserModel(
    persistentModel: PersistentModel = PersistentModel(),
    emailAddress: String? = null,
    person: PersonModel? = null,
    username: String? = null,
    usertype: UserModel.Usertype = UserModel.Usertype.ACTIVE,
) = UserModel(
    persistentModel = persistentModel,
    person = person,
    emailAddress = emailAddress,
    username = username,
    usertype = usertype,
)
