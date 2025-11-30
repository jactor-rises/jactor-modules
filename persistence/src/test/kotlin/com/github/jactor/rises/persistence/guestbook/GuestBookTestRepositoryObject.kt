package com.github.jactor.rises.persistence.guestbook

import com.github.jactor.rises.persistence.util.toGuestBookDao
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.util.UUID

object GuestBookTestRepositoryObject {
    fun findGuestBookByUserId(id: UUID): GuestBookDao? = GuestBooks.selectAll()
        .andWhere { GuestBooks.userId eq id }
        .singleOrNull()?.toGuestBookDao()
}
