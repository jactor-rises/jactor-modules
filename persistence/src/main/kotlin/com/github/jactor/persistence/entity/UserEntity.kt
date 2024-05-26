package com.github.jactor.persistence.entity

import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import com.github.jactor.persistence.blog.BlogEntity
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.PersistentEntity
import com.github.jactor.persistence.dto.UserModel
import com.github.jactor.persistence.guestbook.GuestBookEntity
import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "T_USER")
class UserEntity : PersistentEntity<UserEntity?> {
    @Id
    override var id: UUID? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    lateinit var persistentDataEmbeddable: PersistentDataEmbeddable
        internal set

    @Column(name = "EMAIL")
    var emailAddress: String? = null

    @Column(name = "USER_NAME", nullable = false)
    var username: String? = null

    @JoinColumn(name = "PERSON_ID")
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var person: PersonEntity? = null
        internal set

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
        blogs = user.blogs.map { it.copyWithoutId() }.toMutableSet()
        emailAddress = user.emailAddress
        guestBook = user.guestBook?.copyWithoutId()
        id = user.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        person = user.person?.copyWithoutId()
        username = user.username
        userType = user.userType
    }

    constructor(user: UserModel) {
        addValues(user)
    }

    private fun addValues(user: UserModel) {
        emailAddress = user.emailAddress
        id = user.persistentModel.id
        persistentDataEmbeddable = PersistentDataEmbeddable(user.persistentModel)
        person = user.person?.let { PersonEntity(it) }
        username = user.username
        userType = UserType.entries
            .firstOrNull { aUserType: UserType -> aUserType.name == user.usertype.name }
            ?: throw IllegalArgumentException("Unknown UserType: " + user.usertype)
    }

    fun toModel(): UserModel {
        return UserModel(
            persistentDataEmbeddable.toModel(id),
            person?.toModel(),
            emailAddress,
            username
        )
    }

    fun fetchPerson(): PersonEntity {
        person?.addUser(this)
        return person ?: error("No person provided to the user entity")
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

    fun update(userModel: UserModel): UserEntity {
        addValues(userModel)
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
        return blogs
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
}
