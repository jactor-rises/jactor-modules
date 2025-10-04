package com.github.jactor.persistence.test

import java.util.UUID
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.GuestBook
import com.github.jactor.persistence.GuestBookEntry
import com.github.jactor.persistence.Person
import com.github.jactor.persistence.User
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.PersistentDto
import com.github.jactor.shared.api.UserDto

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
