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
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.PersistentEntity
import com.github.jactor.persistence.common.PersistentModel
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
    fun createWhenNotExists(person: PersonModel): PersonEntity? {
        return findExisting(person) ?: create(person)
    }

    private fun create(person: PersonModel): PersonEntity {
        return personRepository.save(
            PersonEntity(
                person = person.copy(
                    persistentModel = person.persistentModel.copy(
                        id = person.id ?: UUID.randomUUID()
                    )
                )
            )
        )
    }

    private fun findExisting(person: PersonModel): PersonEntity? {
        return person.id?.let { personRepository.findById(it).getOrNull() }
    }
}

@JvmRecord
data class PersonModel(
    val persistentModel: PersistentModel = PersistentModel(),
    val address: AddressModel? = null,
    val locale: String? = null,
    val firstName: String? = null,
    val surname: String = "",
    val description: String? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(
        persistentModel: PersistentModel, person: PersonModel
    ) : this(
        persistentModel = persistentModel,
        address = person.address,
        description = person.description,
        firstName = person.firstName,
        locale = person.locale,
        surname = person.surname
    )

    constructor(personDto: PersonDto) : this(
        persistentModel = PersistentModel(persistentDto = personDto.persistentDto),
        address = if (personDto.address != null) AddressModel(personDto.address!!) else null,
        description = personDto.description,
        firstName = personDto.firstName,
        locale = personDto.locale,
        surname = personDto.surname
    )

    fun toPersonDto() = PersonDto(
        persistentDto = persistentModel.toDto(),
        address = address?.toAddressDto(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )
}

internal object PersonBuilder {
    fun new(personModel: PersonModel = PersonModel()): PersonData = PersonData(
        personModel = personModel.copy(
            persistentModel = personModel.persistentModel.copy(id = UUID.randomUUID())
        )
    )

    fun unchanged(personModel: PersonModel): PersonData = PersonData(
        personModel = personModel
    )

    @JvmRecord
    data class PersonData(val personModel: PersonModel) {
        fun build(): PersonEntity = PersonEntity(person = personModel)
    }
}

interface PersonRepository : CrudRepository<PersonEntity, UUID> {
    fun findBySurname(surname: String?): List<PersonEntity>
}

@Entity
@Table(name = "T_PERSON")
class PersonEntity : PersistentEntity<PersonEntity?> {
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
    private var users: MutableSet<UserEntity> = HashSet()

    constructor() {
        // used by entity manager
    }

    private constructor(person: PersonEntity) {
        addressEntity = person.addressEntity
        description = person.description
        firstName = person.firstName
        locale = person.locale
        id = person.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        surname = person.surname
        users = person.users
    }

    constructor(person: PersonModel) {
        addressEntity = person.address?.let { AddressEntity(it) }
        description = person.description
        firstName = person.firstName
        locale = person.locale
        id = person.id
        persistentDataEmbeddable = PersistentDataEmbeddable(person.persistentModel)
        surname = person.surname
    }

    fun toModel() = PersonModel(
        persistentModel = persistentDataEmbeddable.toModel(id),
        address = addressEntity?.toModel(),
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )

    override fun copyWithoutId(): PersonEntity {
        val personEntity = PersonEntity(this)
        personEntity.id = null
        return personEntity
    }

    override fun modifiedBy(modifier: String): PersonEntity {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other != null && javaClass == other.javaClass &&
            addressEntity == (other as PersonEntity).addressEntity &&
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

    fun getUsers(): Set<UserEntity> {
        return users
    }

    fun addUser(user: UserEntity) {
        users.add(user)
    }
}
