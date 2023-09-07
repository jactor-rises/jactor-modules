package com.github.jactor.persistence.entity

import java.time.LocalDateTime
import java.util.Collections
import java.util.Objects
import java.util.stream.Collectors
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import com.github.jactor.persistence.dto.UserInternalDto
import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "T_USER")
class UserEntity : PersistentEntity<UserEntity?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    @SequenceGenerator(name = "userSeq", sequenceName = "T_USER_SEQ", allocationSize = 1)
    override var id: Long? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    private lateinit var persistentDataEmbeddable: PersistentDataEmbeddable

    @Column(name = "EMAIL")
    var emailAddress: String? = null

    @Column(name = "USER_NAME", nullable = false)
    var username: String? = null

    @JoinColumn(name = "PERSON_ID")
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var person: PersonEntity? = null
        private set

    @OneToOne(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    var guestBook: GuestBookEntity? = null
        private set

    @OneToMany(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    private var blogs: MutableSet<BlogEntity> = HashSet()

    @Column(name = "USER_TYPE")
    @Enumerated(EnumType.STRING)
    private var userType: UserType? = null

    constructor() {
        // used by entity manager
    }

    /**
     * @param user is used to create an entity
     */
    private constructor(user: UserEntity) {
        blogs = user.blogs.stream().map { obj: BlogEntity -> obj.copyWithoutId() }.collect(Collectors.toSet())
        emailAddress = user.emailAddress
        guestBook = user.guestBook?.copyWithoutId()
        id = user.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        person = user.person?.copyWithoutId()
        username = user.username
        userType = user.userType
    }

    constructor(user: UserInternalDto) {
        addValues(user)
    }

    private fun addValues(user: UserInternalDto) {
        emailAddress = user.emailAddress
        id = user.id
        persistentDataEmbeddable = PersistentDataEmbeddable(user.persistentDto)
        person = user.person?.let { PersonEntity(it) }
        username = user.username
        userType = UserType.entries
            .firstOrNull { aUserType: UserType -> aUserType.name == user.usertype.name }
            ?: throw IllegalArgumentException("Unknown UserType: " + user.usertype)
    }

    fun asDto(): UserInternalDto {
        return UserInternalDto(
            persistentDataEmbeddable.asPersistentDto(id),
            person?.asDto(),
            emailAddress,
            username
        )
    }

    fun fetchPerson(): PersonEntity? {
        person!!.addUser(this)
        return person
    }

    override fun copyWithoutId(): UserEntity {
        val userEntity = UserEntity(this)
        userEntity.id = null
        return userEntity
    }

    override fun modifiedBy(modifier: String): UserEntity {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    fun add(blogEntity: BlogEntity) {
        blogs.add(blogEntity)
        blogEntity.user = this
    }

    fun update(userInternalDto: UserInternalDto): UserEntity {
        addValues(userInternalDto)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return other === this || other != null && javaClass == other.javaClass &&
            emailAddress == (other as UserEntity).emailAddress &&
            person == other.person &&
            username == other.username
    }

    override fun hashCode(): Int {
        return Objects.hash(username, person, emailAddress)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .appendSuper(super.toString())
            .append(username)
            .append(emailAddress)
            .append(blogs)
            .append("guestbook.id=" + if (guestBook != null) guestBook!!.id else null)
            .append(person)
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

    fun getBlogs(): Set<BlogEntity> {
        return Collections.unmodifiableSet(blogs)
    }

    fun setGuestBook(guestBook: GuestBookEntity) {
        this.guestBook = guestBook
        guestBook.user = this
    }

    fun setPersonEntity(personEntity: PersonEntity?) {
        person = personEntity
    }

    enum class UserType {
        ADMIN, ACTIVE, INACTIVE
    }

    companion object {
        @JvmStatic
        fun aUser(userInternalDto: UserInternalDto): UserEntity {
            return UserEntity(userInternalDto)
        }
    }
}
