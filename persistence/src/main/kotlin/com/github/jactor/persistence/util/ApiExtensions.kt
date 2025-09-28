package com.github.jactor.persistence.util

import java.time.LocalDate
import java.time.LocalDateTime
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.CreateBlogEntry
import com.github.jactor.persistence.GuestBook
import com.github.jactor.persistence.GuestBookEntry
import com.github.jactor.persistence.Person
import com.github.jactor.persistence.UpdateBlogTitle
import com.github.jactor.persistence.User
import com.github.jactor.persistence.UserDao
import com.github.jactor.persistence.UserDao.UserType
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.CreateBlogEntryCommand
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UpdateBlogTitleCommand
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.whenTrue

fun AddressDto.toAddress() = Address(
    persistent = persistentDto.toPersistent(),

    addressLine1 = requireNotNull(addressLine1) { "Address line 1 cannot be null!" },
    addressLine2 = addressLine2,
    addressLine3 = addressLine3,
    city = requireNotNull(city) { "City cannot be null!" },
    country = country,
    zipCode = requireNotNull(zipCode) { "Zip code cannot be null!" },
)

fun BlogDto.toBlog() = Blog(
    persistent = persistentDto.toPersistent(),

    created = persistentDto.isWithId.whenTrue { LocalDate.now() },
    title = requireNotNull(title) { "Title cannot be null!" },
    user = requireNotNull(user?.toUser()) { "User cannot be null!" },
)

private val PersistentDto.isWithId: Boolean get() = id != null

fun BlogEntryDto.toBlogEntry() = BlogEntry(
    persistent = persistentDto.toPersistent(),

    blog = blogDto?.toBlog(),
    creatorName = creatorName,
    entry = entry,
)

fun GuestBookDto.toGuestBook() = GuestBook(
    persistent = persistentDto.toPersistent(),

    entries = emptySet(),
    title = requireNotNull(title) { "Title cannot be null!" },
    user = requireNotNull(userDto?.toUser()) { "User cannot be null!" },
).let { parent -> parent.copy(entries = entries.map { it.toGuestBookEntry(parent = parent) }.toSet()) }

fun CreateBlogEntryCommand.toCreateBlogEntry() = CreateBlogEntry(
    blogId = requireNotNull(blogId) { "Blog ID cannot be null!" },
    creatorName = requireNotNull(creatorName) { "Creator name cannot be null!" },
    entry = requireNotNull(entry) { "Entry cannot be null!" }
)

fun GuestBookEntryDto.toGuestBookEntry(parent: GuestBook?) = GuestBookEntry(
    persistent = persistentDto.toPersistent(),

    creatorName = requireNotNull(creatorName) { "Creator name cannot be null!" },
    entry = requireNotNull(entry) { "Entry cannot be null!" },
    guestBook = parent
)

fun com.github.jactor.shared.api.UserType.toModel(): UserType = UserType.entries
    .firstOrNull { it.name == this.name } ?: error("User type ${this.name} not found!")

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

fun UpdateBlogTitleCommand.toUpdateBlogTitle() = UpdateBlogTitle(
    blogId = requireNotNull(blogId) { "Blog ID cannot be null!" },
    title = requireNotNull(title) { "Title cannot be null!" }
)

fun UserDto.toUser() = User(
    persistent = persistentDto.toPersistent(),

    person = person?.toPerson(),
    emailAddress = emailAddress,
    username = username,
    usertype = User.Usertype.valueOf(userType.name)
)

fun UserDto.toUserDao() = UserDao(
    id = persistentDto.id,
    createdBy = requireNotNull(persistentDto.createdBy) { "Created by cannot be null!" },
    timeOfCreation = persistentDto.timeOfCreation ?: LocalDateTime.now(),
    modifiedBy = requireNotNull(persistentDto.modifiedBy) { "Modified by cannot be null!" },
    timeOfModification = persistentDto.timeOfModification ?: LocalDateTime.now(),
    userType = userType.toModel(),
    emailAddress = null,
    personId = null,
    username = "na",
)
