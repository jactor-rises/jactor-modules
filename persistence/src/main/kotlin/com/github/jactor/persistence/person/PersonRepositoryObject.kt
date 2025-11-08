package com.github.jactor.persistence.person

import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update

object PersonRepositoryObject : PersonRepository {
    override fun findAll(): List<PersonDao> = People.selectAll().map { it.toPersonDao() }

    override fun findById(id: UUID): PersonDao? = People
        .selectAll()
        .andWhere { People.id eq id }
        .map { it.toPersonDao() }
        .singleOrNull()

    override fun findBySurname(surname: String?): List<PersonDao> = when {
        (surname?.isBlank() ?: true) -> emptyList()

        else -> People
            .selectAll()
            .andWhere { People.surname eq surname }
            .map { it.toPersonDao() }
    }

    private fun ResultRow.toPersonDao() = PersonDao(
        id = this[People.id].value,

        createdBy = this[People.createdBy],
        modifiedBy = this[People.modifiedBy],
        timeOfCreation = this[People.timeOfCreation],
        timeOfModification = this[People.timeOfModification],

        description = this[People.description],
        firstName = this[People.firstName],
        surname = this[People.surname],
        locale = this[People.locale],
        addressId = this[People.addressId],
    )

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
