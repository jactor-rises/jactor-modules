package com.github.jactor.rises.persistence.guestbook

import com.github.jactor.rises.persistence.util.toGuestBookDao
import com.github.jactor.rises.persistence.util.toGuestBookEntryDao
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

object GuestBookRepositoryObject : GuestBookRepository {
    override fun findAllGuestBooks(): List<GuestBookDao> = GuestBooks.selectAll().map { it.toGuestBookDao() }
    override fun findByUserId(userId: UUID): GuestBookDao? = GuestBooks.selectAll()
        .andWhere { GuestBooks.userId eq userId }
        .map { it.toGuestBookDao() }
        .singleOrNull()

    override fun findGuestBookById(id: UUID): GuestBookDao? = GuestBooks.selectAll()
        .andWhere { GuestBooks.id eq id }
        .singleOrNull()?.toGuestBookDao()

    override fun findGuestBookEntryById(id: UUID): GuestBookEntryDao? = GuestBookEntries.selectAll()
        .andWhere { GuestBookEntries.id eq id }
        .singleOrNull()?.toGuestBookEntryDao()

    override fun findGuestBookEtriesByGuestBookId(id: UUID): List<GuestBookEntryDao> = GuestBookEntries.selectAll()
        .andWhere { GuestBookEntries.guestBookId eq id }
        .map { it.toGuestBookEntryDao() }

    override fun save(guestBookDao: GuestBookDao): GuestBookDao =
        when (guestBookDao.isPersisted) {
            true -> update(guestBookDao)
            false -> insert(guestBookDao)
        }

    private fun update(guestBookDao: GuestBookDao): GuestBookDao = GuestBooks.update(
        where = { GuestBooks.id eq guestBookDao.id },
    ) {
        it[createdBy] = guestBookDao.createdBy
        it[title] = guestBookDao.title
        it[modifiedBy] = guestBookDao.modifiedBy
        it[timeOfCreation] = guestBookDao.timeOfCreation
        it[timeOfModification] = guestBookDao.timeOfModification
        it[userId] = requireNotNull(guestBookDao.userId) { "UserId cannot be null!" }
    }.let { guestBookDao }

    private fun insert(guestBookDao: GuestBookDao): GuestBookDao = GuestBooks.insertAndGetId {
        it[createdBy] = guestBookDao.createdBy
        it[title] = guestBookDao.title
        it[modifiedBy] = guestBookDao.modifiedBy
        it[timeOfCreation] = guestBookDao.timeOfCreation
        it[timeOfModification] = guestBookDao.timeOfModification
        it[userId] = requireNotNull(guestBookDao.userId) { "UserId cannot be null!" }
    }.value.let { guestBookDao.copy(id = it) }

    override fun save(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao = when (guestBookEntryDao.isPersisted) {
        true -> update(guestBookEntryDao)
        false -> insert(guestBookEntryDao)
    }

    private fun update(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao = GuestBookEntries.update(
        where = { GuestBookEntries.id eq guestBookEntryDao.id },
    ) {
        it[createdBy] = guestBookEntryDao.createdBy
        it[guestBookId] = requireNotNull(guestBookEntryDao.guestBookId) { "GuestBookId cannot be null!" }
        it[guestName] = guestBookEntryDao.guestName
        it[entry] = guestBookEntryDao.entry
        it[modifiedBy] = guestBookEntryDao.modifiedBy
        it[timeOfCreation] = guestBookEntryDao.timeOfCreation
        it[timeOfModification] = guestBookEntryDao.timeOfModification
    }.let { guestBookEntryDao }

    private fun insert(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao = GuestBookEntries.insertAndGetId {
        it[createdBy] = guestBookEntryDao.createdBy
        it[guestBookId] = requireNotNull(guestBookEntryDao.guestBookId) { "GuestBookId cannot be null!" }
        it[guestName] = guestBookEntryDao.guestName
        it[entry] = guestBookEntryDao.entry
        it[modifiedBy] = guestBookEntryDao.modifiedBy
        it[timeOfCreation] = guestBookEntryDao.timeOfCreation
        it[timeOfModification] = guestBookEntryDao.timeOfModification
    }.value.let { guestBookEntryDao.copy(id = it) }
}
