package com.github.jactor.rises.persistence.person

import java.util.UUID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import com.github.jactor.rises.persistence.util.toPersonDao

object PersonRepositoryObject : PersonRepository {
    override fun findById(id: UUID): PersonDao? = People
        .selectAll()
        .andWhere { People.id eq id }
        .map { it.toPersonDao() }
        .singleOrNull()

    override fun save(personDao: PersonDao): PersonDao = when (personDao.isNotPersisted) {
        true -> insert(personDao)
        false -> update(personDao)
    }

    private fun insert(personDao: PersonDao): PersonDao = People.insertAndGetId { row ->
        row[createdBy] = personDao.createdBy
        row[modifiedBy] = personDao.modifiedBy
        row[timeOfCreation] = personDao.timeOfCreation
        row[timeOfModification] = personDao.timeOfModification
        row[description] = personDao.description
        row[firstName] = personDao.firstName
        row[surname] = personDao.surname
        row[locale] = personDao.locale
        personDao.addressId?.let {
            row[addressId] = it
        }
    }.let { newId -> personDao.also { it.id = newId.value } }

    private fun update(personDao: PersonDao): PersonDao = People.update(
        where = { People.id eq personDao.id }
    ) { row ->
        row[modifiedBy] = personDao.modifiedBy
        row[timeOfModification] = personDao.timeOfModification
        row[description] = personDao.description
        row[firstName] = personDao.firstName
        row[surname] = personDao.surname
        row[locale] = personDao.locale

        personDao.addressId?.let {
            row[addressId] = it
        }
    }.let { personDao }
}
