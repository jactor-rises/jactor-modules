package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.stereotype.Service
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.common.DaoRelation
import com.github.jactor.persistence.common.DaoRelations
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.shared.api.PersonDto

@Service
class PersonService(private val personRepository: PersonRepository) {
    suspend fun createWhenNotExists(person: Person): PersonDao? = findExisting(person) ?: create(person)
    private suspend fun create(person: Person): PersonDao = personRepository.save(personDao = person.toPersonDao())
    private suspend fun findExisting(person: Person): PersonDao? = person.id?.let {
        personRepository.findById(it)
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

    fun toPersonDao() = PersonDao(

    )

    fun toPersonDto() = PersonDto(
        persistentDto = persistent.toPersistentDto(),
        address = address?.toAddressDto(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )
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

interface PersonRepository {
    fun findAll(): List<PersonDao>
    fun findById(id: UUID): PersonDao?
    fun findBySurname(surname: String?): List<PersonDao>
    fun save(personDao: PersonDao): PersonDao
}

object PersonRepositoryObject : PersonRepository {
    override fun findAll(): List<PersonDao> = transaction {
        People.selectAll().map { it.toPersonDao() }
    }

    override fun findById(id: UUID): PersonDao? = People
        .selectAll()
        .andWhere { People.id eq id }
        .map { it.toPersonDao() }
        .singleOrNull()

    override fun findBySurname(surname: String?): List<PersonDao> = when {
        (surname?.isNotBlank() ?: true) -> emptyList()

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

    override fun save(personDao: PersonDao): PersonDao = transaction {
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
    private val addressRelation = DaoRelation(fetchRelation = JactorPersistenceRepositiesConfig.fetchAddressRelation)
    private val userRelations = DaoRelations(fetchRelations = JactorPersistenceRepositiesConfig.fetchUserRelations)
    val users: List<UserDao> get() = id?.let { userRelations.fetchRelations(id = it) } ?: emptyList()
    val addressDao: AddressDao? get() = addressRelation.fetchRelatedInstance(id = addressId)

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
