package com.github.jactor.persistence.test

import java.time.LocalDate
import java.util.UUID
import com.github.jactor.persistence.AddressEntity
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogEntry
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

fun initAddress(
    persistent: Persistent = Persistent(),
    addressLine1: String? = null,
    addressLine2: String? = null,
    addressLine3: String? = null,
    city: String? = null,
    country: String? = null,
    zipCode: String? = null,
) = Address(
    persistent = persistent,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    addressLine3 = addressLine3,
    city = city,
    country = country,
    zipCode = zipCode
)

fun initBlog(
    created: LocalDate? = null,
    persistent: Persistent = Persistent(),
    title: String? = null,
    user: User? = null,
) = Blog(
    created = created,
    persistent = persistent,
    title = title,
    user = user,
)

fun initBlogEntry(
    blog: Blog? = null,
    creatorName: String? = null,
    entry: String? = null,
    persistent: Persistent = Persistent(),
) = BlogEntry(
    blog = blog,
    persistent = persistent,
    creatorName = creatorName,
    entry = entry,
)

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
    id: UUID? = null,
    persistent: Persistent = Persistent(),
    address: Address? = null,
    description: String? = null,
    firstName: String? = null,
    locale: String? = null,
    surname: String = "Doe",
) = Person(
    persistent = id?.let { persistent.copy(id = id) } ?: persistent,
    address = address,
    firstName = firstName,
    description = description,
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
