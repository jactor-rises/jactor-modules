package com.github.jactor.persistence.test

import java.util.UUID
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.GuestBook
import com.github.jactor.persistence.GuestBookEntry
import com.github.jactor.persistence.Person
import com.github.jactor.persistence.User

fun Address.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun Blog.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun BlogEntry.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun GuestBook.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun GuestBookEntry.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun Person.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
fun User.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
