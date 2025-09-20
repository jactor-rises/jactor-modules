package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.stereotype.Service
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.shared.api.PersonDto

@Service
class PersonService() {
    suspend fun createWhenNotExists(person: Person): PersonDao? = findExisting(person) ?: create(person)
    private suspend fun create(person: Person): PersonDao = PersonRepository.insertOrUpdate(PersonDao(person = person))
    private suspend fun findExisting(person: Person): PersonDao? = person.id?.let {
        PersonRepository.findById(it)
    }
}

@JvmRecord
data class Person(
    val persistent: Persistent,
    val address: Address?,
    val locale: String?,
    val firstName: String?,
    val surname: String,
    val description: String?,
) {
    val id: UUID? @JsonIgnore get() = persistent.id

    constructor(
        persistent: Persistent, person: Person
    ) : this(
        persistent = persistent,
        address = person.address,
        description = person.description,
        firstName = person.firstName,
        locale = person.locale,
        surname = person.surname
    )

    constructor(personDto: PersonDto) : this(
        persistent = personDto.persistentDto.toPersistent(),
        address = personDto.address?.toAddress(),
        description = personDto.description,
        firstName = personDto.firstName,
        locale = personDto.locale,
        surname = personDto.surname
    )

    fun toPersonDto() = PersonDto(
        persistentDto = persistent.toPersistentDto(),
        address = address?.toAddressDto(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )

    fun withId(): Person = copy(persistent = persistent.copy(id = id ?: UUID.randomUUID()))
    fun toEntity() = PersonDao(person = this)
    fun toEntityWithId() = PersonDao(person = this).apply { id = UUID.randomUUID() }
}

object People : UUIDTable(name = "T_PERSON", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val description = text("DESCRIPTION").nullable()
    val firstName = text("FIRST_NAME").nullable()
    val surname = text("SURNAME")
    val locale = text("LOCALE").nullable()
    val addressId = uuid("ADDRESS_ID").references(Addresses.id)
}

object PersonRepository {
    fun findById(id: UUID): PersonDao? = People
        .selectAll()
        .where { People.id eq id }
        .map { it.toPersonDao() }
        .firstOrNull()

    fun findBySurname(surname: String?): List<PersonDao> = when {
        (surname?.isNotBlank() ?: true) -> emptyList()

        else -> People
            .selectAll()
            .where { People.surname eq surname }
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

    fun insertOrUpdate(personDao: PersonDao): PersonDao = transaction {
        when (personDao.isIdNull()) {
            true -> insert(personDao)
            false -> update(personDao)
        }
    }

    private fun insert(personDao: PersonDao): PersonDao = transaction {
        People.insertAndGetId { row ->
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
    }

    private fun update(personDao: PersonDao): PersonDao = transaction {
        People.update(where = { People.id eq personDao.id }) { row ->
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
}

data class PersonDao(
    override var id: UUID? = null,
    override var createdBy: String = "todo",
    override var timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var modifiedBy: String = "todo",
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    var description: String? = null,
    var firstName: String? = null,
    var locale: String? = null,
    var surname: String = "",
    var addressId: UUID? = null,
) : PersistentDao<PersonDao> {
    var addressDao: AddressDao?
        get() = addressId?.let { AddressRepository.findById(addressId = it) }
        set(value) {
            addressId = value?.id
        }

    constructor(person: Person) : this(
        id = person.id,

        createdBy = person.persistent.createdBy,
        modifiedBy = person.persistent.modifiedBy,
        timeOfCreation = person.persistent.timeOfCreation,
        timeOfModification = person.persistent.timeOfModification,

        addressId = person.address?.persistent?.id,
        description = person.description,
        firstName = person.firstName,
        locale = person.locale,
        surname = person.surname,
    )

    fun toPerson() = Person(
        persistent = toPersistent(),
        address = addressDao?.toAddress(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )

    override fun copyWithoutId(): PersonDao = copy(
        id = null,
        addressId = null,
    )

    override fun modifiedBy(modifier: String): PersonDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }
}
