package com.github.jactor.persistence.test

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.AddressDao
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogDao
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.BlogEntryDao
import com.github.jactor.persistence.GuestBook
import com.github.jactor.persistence.GuestBookDao
import com.github.jactor.persistence.GuestBookEntry
import com.github.jactor.persistence.GuestBookEntryDao
import com.github.jactor.persistence.Person
import com.github.jactor.persistence.PersonDao
import com.github.jactor.persistence.User
import com.github.jactor.persistence.UserDao
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.CreateUserCommand
import kotlin.String

fun initAddress(
    persistent: Persistent = Persistent(),
    addressLine1: String = "na",
    addressLine2: String? = null,
    addressLine3: String? = null,
    city: String = "na",
    country: String? = null,
    zipCode: String = "na",
) = Address(
    persistent = persistent,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    addressLine3 = addressLine3,
    city = city,
    country = country,
    zipCode = zipCode
)

fun initAddressDao(
    id: UUID? = null,
    createdBy: String = "unit test",
    timeOfCreation: LocalDateTime = LocalDateTime.now(),
    modifiedBy: String = "unit test",
    timeOfModification: LocalDateTime = LocalDateTime.now(),
    addressLine1: String = "na",
    addressLine2: String? = null,
    addressLine3: String? = null,
    city: String = "na",
    country: String? = null,
    zipCode: String = "na",
) = AddressDao(
    id = id,
    createdBy = createdBy,
    timeOfCreation = timeOfCreation,
    modifiedBy = modifiedBy,
    timeOfModification = timeOfModification,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    addressLine3 = addressLine3,
    city = city,
    country = country,
    zipCode = zipCode,
)

fun initBlog(
    created: LocalDate? = null,
    persistent: Persistent = Persistent(),
    title: String = "na",
    user: User? = null,
) = Blog(
    created = created,
    persistent = persistent,
    title = title,
    user = user,
)

fun initBlogDao(
    id: UUID? = null,
    timeOfModification: LocalDateTime = LocalDateTime.now()
) = BlogDao(
    id = id,
    createdBy = "unit test",
    modifiedBy = "unit test",
    timeOfCreation = LocalDateTime.now(),
    timeOfModification = timeOfModification,
)

fun initBlogEntry(
    blog: Blog = initBlog(),
    creatorName: String = "na",
    entry: String = "na",
    persistent: Persistent = Persistent(),
) = BlogEntry(
    blog = blog,
    persistent = persistent,
    creatorName = creatorName,
    entry = entry,
)

fun initBlogEntryDao(
    id: UUID? = null,
    timeOfModification: LocalDateTime = LocalDateTime.now(),
    blogId: UUID = UUID.randomUUID(),
) = BlogEntryDao(
    id = id,
    createdBy = "unit test",
    creatorName = "unit test",
    entry = "unit test",
    modifiedBy = "unit test",
    timeOfCreation = LocalDateTime.now(),
    timeOfModification = timeOfModification,
    blogId = blogId
)

fun initCreateUserCommand(
    personId: UUID? = null,
    username: String = "noway",
    surname: String = "dracula",
) = CreateUserCommand(
    personId = personId,
    username = username,
    surname = surname,
)

fun initGuestBook(
    entries: Set<GuestBookEntry> = emptySet(),
    persistent: Persistent = Persistent(),
    title: String? = null,
    user: User? = null,
) = GuestBook(
    entries = entries,
    persistent = persistent,
    title = title,
    user = user,
)

fun initGuestBookDao(id: UUID? = null, timeOfModification: LocalDateTime = LocalDateTime.now()) = GuestBookDao(
    id = id,
    timeOfModification = timeOfModification,
)

fun initGuestBookEntry(
    creatorName: String = "na",
    entry: String = "na",
    guestBook: GuestBook? = null,
    persistent: Persistent = Persistent(),
) = GuestBookEntry(
    creatorName = creatorName,
    entry = entry,
    guestBook = guestBook,
    persistent = persistent,
)

fun initGuestBookEntryDao(
    id: UUID? = null,
    timeOfModification: LocalDateTime = LocalDateTime.now(),
) = GuestBookEntryDao(
    id = id,
    createdBy = "unit test",
    creatorName = "unit test",
    entry = "unit test",
    guestBookId = null,
    modifiedBy = "unit test",
    timeOfCreation = LocalDateTime.now(),
    timeOfModification = timeOfModification,
)

fun initUserDao(
    id: UUID? = UUID.randomUUID(),
    personId: UUID? = null,
    username: String = "whoami",
    timeOfModification: LocalDateTime = LocalDateTime.now(),
): UserDao = UserDao(
    id = id,

    createdBy = "unit test",
    emailAddress = null,
    modifiedBy = "unit test",
    personId = personId,
    username = username,
    userType = UserDao.UserType.ACTIVE,
    timeOfCreation = LocalDateTime.now(),
    timeOfModification = timeOfModification,
)

fun initUserDao(createUserCommand: CreateUserCommand) = UserDao(
    id = UUID.randomUUID(),

    createdBy = createUserCommand.username,
    emailAddress = null,
    modifiedBy = createUserCommand.username,
    personId = createUserCommand.personId,
    username = createUserCommand.username,
    userType = UserDao.UserType.ACTIVE,
    timeOfCreation = LocalDateTime.now(),
    timeOfModification = LocalDateTime.now(),
)

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

fun initPersonDao(
    id: UUID? = null,
    addressId: UUID? = null,
    description: String? = null,
    firstName: String? = null,
    locale: String? = null,
    surname: String = "",
    timeOfModification: LocalDateTime = LocalDateTime.now(),
) = PersonDao(
    id = id,
    addressId = addressId,
    description = description,
    firstName = firstName,
    locale = locale,
    surname = surname,
    timeOfModification = timeOfModification,
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

fun timestamped(username: String): String {
    return "$username@${java.lang.Long.toHexString(System.currentTimeMillis())}"
}
