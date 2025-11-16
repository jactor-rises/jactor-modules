package com.github.jactor.rises.persistence.guestbook

import java.util.UUID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll

object GuestBookTestRepositoryObject {
    fun findGuestBookByUserId(id: UUID): GuestBookDao? = GuestBooks.selectAll()
        .andWhere { GuestBooks.userId eq id }
        .singleOrNull()?.toGuestBookDao()
}
