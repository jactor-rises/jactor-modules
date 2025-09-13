package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.Config.ioContext
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.PersonDto
import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

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
        persistentDto = persistent.toDto(),
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

interface PersonRepository : CrudRepository<PersonDao, UUID> {
    fun findBySurname(surname: String?): List<PersonDao>
}

@Entity
@Table(name = "T_PERSON")
class PersonDao : PersistentDao<PersonDao?> {
    @Id
    override var id: UUID? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    lateinit var persistentDataEmbeddable: PersistentDataEmbeddable
        internal set

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "FIRST_NAME")
    var firstName: String? = null

    @Column(name = "LOCALE")
    var locale: String? = null

    @Column(name = "SURNAME", nullable = false)
    var surname: String = ""

    @JoinColumn(name = "ADDRESS_ID")
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var addressEntity: AddressEntity? = null
        internal set

    @OneToMany(mappedBy = "person", cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.EAGER)
    private var users: MutableSet<UserDao> = HashSet()

    constructor() {
        // used by entity manager
    }

    private constructor(person: PersonDao) {
        addressEntity = person.addressEntity
        description = person.description
        firstName = person.firstName
        locale = person.locale
        id = person.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        surname = person.surname
        users = person.users
    }

    constructor(person: Person) {
        addressEntity = person.address?.let { AddressEntity(it) }
        description = person.description
        firstName = person.firstName
        locale = person.locale
        id = person.id
        persistentDataEmbeddable = PersistentDataEmbeddable(person.persistent)
        surname = person.surname
    }

    fun toModel() = Person(
        persistent = persistentDataEmbeddable.toModel(id),
        address = addressEntity?.toModel(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )

    override fun copyWithoutId(): PersonDao {
        val personEntity = PersonDao(this)
        personEntity.id = null
        return personEntity
    }

    override fun modifiedBy(modifier: String): PersonDao {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other != null && javaClass == other.javaClass &&
            addressEntity == (other as PersonDao).addressEntity &&
            description == other.description &&
            firstName == other.firstName &&
            surname == other.surname &&
            locale == other.locale
    }

    override fun hashCode(): Int {
        return Objects.hash(addressEntity, description, firstName, surname, locale)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .appendSuper(super.toString())
            .append(firstName)
            .append(surname)
            .append(getUsers())
            .append(addressEntity)
            .toString()
    }

    override val createdBy: String
        get() = persistentDataEmbeddable.createdBy
    override val timeOfCreation: LocalDateTime
        get() = persistentDataEmbeddable.timeOfCreation
    override val modifiedBy: String
        get() = persistentDataEmbeddable.modifiedBy
    override val timeOfModification: LocalDateTime
        get() = persistentDataEmbeddable.timeOfModification

    fun getUsers(): Set<UserDao> {
        return users
    }

    fun addUser(user: UserDao) {
        users.add(user)
    }
}
