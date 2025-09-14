package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.jetbrains.exposed.v1.core.ResultRow
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.shared.api.PersonDto

@Service
class PersonService(private val personRepository: PersonRepository) {
    suspend fun createWhenNotExists(person: Person): PersonDao? = ioContext {
        findExisting(person) ?: create(person)
    }

    private suspend fun create(person: Person): PersonDao = ioContext {
        personRepository.save(PersonDao(person = person.withId()))
    }

    private suspend fun findExisting(person: Person): PersonDao? = ioContext {
        person.id?.let { personRepository.findById(it).getOrNull() }
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
        persistent = Persistent(persistentDto = personDto.persistentDto),
        address = if (personDto.address != null) Address(personDto.address!!) else null,
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
    fun toEntityWithId() = PersonDao(person = this).apply {
        id = UUID.randomUUID()
        persistentDataEmbeddable = Persistent(id = id).toEmbeddable()
    }
}

@Repository
class PersonRepository {
    fun findBySurname(surname: String?): List<PersonDao> = emptyList()

    fun ResultRow.toUserDao(): UserDao = UserDao(
    )
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
    var addressDao: AddressDao? = null,

    private var users: MutableSet<UserDao> = HashSet(),
) : PersistentDao<PersonDao?> {
    constructor(person: Person) : this(
        id = person.id

            addressDao = person . address ?. let { AddressDao(dao = it) }
            description = person . description
            firstName = person.firstName
            locale = person . locale
            surname = person.surname
    )

    fun toPerson() = Person(
        persistent = persistentDataEmbeddable.toModel(id),
        address = addressDao?.toPerson(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )

    override fun copyWithoutId(): PersonDao  = copy(
        id = null,
        addressDao = addressDao?.copyWithoutId(),
    )

    fun addUser(user: UserDao) {
        users.add(user)
    }
}
