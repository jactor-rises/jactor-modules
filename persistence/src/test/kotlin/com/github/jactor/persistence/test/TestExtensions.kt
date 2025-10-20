package com.github.jactor.persistence.test

import com.github.jactor.persistence.Persistent
import com.github.jactor.persistence.address.Address
import com.github.jactor.persistence.blog.Blog
import com.github.jactor.persistence.blog.BlogEntry
import com.github.jactor.persistence.guestbook.GuestBook
import com.github.jactor.persistence.guestbook.GuestBookEntry
import com.github.jactor.persistence.person.Person
import com.github.jactor.persistence.user.User
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.UserDto
import java.util.UUID

fun Address.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun Blog.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun BlogEntry.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun GuestBook.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun Person.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun User.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun Blog.withPersistentData(): Blog = copy(
    persistent = persistent.withPersistedData()
)

fun GuestBookEntry.withPersistedData(id: UUID): GuestBookEntry = copy(
    persistent = Persistent().withPersistedData(id = id),
)

fun Persistent.withPersistedData(id: UUID? = UUID.randomUUID()) = copy(
    id = id,
    createdBy = "unit test",
    modifiedBy = "user test",
)

fun PersistentDto.withPersistedData(id: UUID? = UUID.randomUUID()) = copy(
    id = id,
    createdBy = "unit test",
    modifiedBy = "user test",
)

fun UserDto.withPersistedData(id: UUID = UUID.randomUUID()): UserDto = copy(
    persistentDto = persistentDto.withPersistedData(id = id)
)
