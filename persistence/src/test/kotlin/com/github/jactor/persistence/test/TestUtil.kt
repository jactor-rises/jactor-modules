package com.github.jactor.persistence.test

import com.github.jactor.persistence.Persistent
import com.github.jactor.persistence.address.Address
import com.github.jactor.persistence.address.AddressDao
import com.github.jactor.persistence.blog.Blog
import com.github.jactor.persistence.blog.BlogDao
import com.github.jactor.persistence.blog.BlogEntry
import com.github.jactor.persistence.blog.BlogEntryDao
import com.github.jactor.persistence.guestbook.GuestBook
import com.github.jactor.persistence.guestbook.GuestBookDao
import com.github.jactor.persistence.guestbook.GuestBookEntry
import com.github.jactor.persistence.guestbook.GuestBookEntryDao
import com.github.jactor.persistence.person.Person
import com.github.jactor.persistence.person.PersonDao
import com.github.jactor.persistence.user.User
import com.github.jactor.persistence.user.UserDao
import com.github.jactor.persistence.user.UserType
import com.github.jactor.shared.api.CreateUserCommand
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.fail

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
    userId: UUID? = null,
) = Blog(
    created = created,
    persistent = persistent,
    title = title,
    userId = userId,
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
    blog: Blog = initBlog().withId(),
    creatorName: String = "na",
    entry: String = "na",
    persistent: Persistent = Persistent(),
) = BlogEntry(
    blogId = blog.id ?: fail { "The blog must be persisted!" },
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
    persistent: Persistent = Persistent(),
    title: String? = null,
    user: User? = null,
) = GuestBook(
    persistent = persistent,
    title = title,
    userId = user?.id,
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
    guestName = creatorName,
    entry = entry,
    guestBookId = guestBook?.id,
    persistent = persistent,
)

fun initGuestBookEntryDao(
    id: UUID? = null,
    timeOfModification: LocalDateTime = LocalDateTime.now(),
) = GuestBookEntryDao(
    id = id,
    createdBy = "unit test",
    guestName = "unit test",
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
    userType = UserType.ACTIVE,
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
    userType = UserType.ACTIVE,
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
    addressId = address?.id,
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
    userType: UserType = UserType.ACTIVE,
) = User(
    persistent = persistent,
    personId = person?.id,
    emailAddress = emailAddress,
    username = username,
    userType = userType,
)

fun timestamped(username: String): String {
    return "$username@${java.lang.Long.toHexString(System.currentTimeMillis())}"
}
