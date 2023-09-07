package com.github.jactor.persistence.entity

import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.PersonInternalDto
import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.time.LocalDateTime
import java.util.Objects
import java.util.Optional

@Entity
@Table(name = "T_PERSON")
class PersonEntity : PersistentEntity<PersonEntity?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSeq")
    @SequenceGenerator(name = "personSeq", sequenceName = "T_PERSON_SEQ", allocationSize = 1)
    override var id: Long? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    private lateinit var persistentDataEmbeddable: PersistentDataEmbeddable

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
        private set

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

    constructor(person: PersonInternalDto) {
        addressEntity = Optional.ofNullable(person.address).map { addressInternalDto: AddressInternalDto? ->
            AddressEntity(
                addressInternalDto!!
            )
        }.orElse(null)
        description = person.description
        firstName = person.firstName
        locale = person.locale
        id = person.id
        persistentDataEmbeddable = PersistentDataEmbeddable(person.persistentDto)
        surname = person.surname
    }

    fun asDto(): PersonInternalDto {
        val addressInternalDto = Optional.ofNullable(addressEntity).map { obj: AddressEntity -> obj.asDto() }
            .orElse(null)

        return PersonInternalDto(
            persistentDataEmbeddable.asPersistentDto(id), addressInternalDto, locale, firstName, surname, description)
    }

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

    companion object {
        @JvmStatic
        fun aPerson(personInternalDto: PersonInternalDto): PersonEntity {
            return PersonEntity(personInternalDto)
        }
    }
}
