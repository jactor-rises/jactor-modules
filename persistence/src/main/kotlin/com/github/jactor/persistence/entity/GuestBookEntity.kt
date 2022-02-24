package com.github.jactor.persistence.entity

import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.dto.UserInternalDto
import javax.persistence.AttributeOverride
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.time.LocalDateTime
import java.util.Objects
import java.util.Optional
import java.util.stream.Collectors

@Entity
@Table(name = "T_GUEST_BOOK")
class GuestBookEntity : PersistentEntity<GuestBookEntity?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guestBookSeq")
    @SequenceGenerator(name = "guestBookSeq", sequenceName = "T_GUEST_BOOK_SEQ", allocationSize = 1)
    override var id: Long? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    private lateinit var persistentDataEmbeddable: PersistentDataEmbeddable

    @Column(name = "TITLE")
    var title: String? = null

    @JoinColumn(name = "USER_ID")
    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    var user: UserEntity? = null

    @OneToMany(mappedBy = "guestBook", cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    private var entries: MutableSet<GuestBookEntryEntity> = HashSet()

    constructor() {
        // used by entity manager
    }

    /**
     * @param guestBook to copyWithoutId...
     */
    private constructor(guestBook: GuestBookEntity) {
        entries = guestBook.entries.stream().map { obj: GuestBookEntryEntity -> obj.copyWithoutId() }.collect(Collectors.toSet())
        id = guestBook.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        title = guestBook.title
        user = guestBook.copyUserWithoutId()
    }

    constructor(guestBook: GuestBookDto) {
        entries = guestBook.entries.stream().map { guestBookEntry: GuestBookEntryDto ->
            GuestBookEntryEntity(
                guestBookEntry
            )
        }.collect(Collectors.toSet())
        id = guestBook.id
        persistentDataEmbeddable = PersistentDataEmbeddable(guestBook.persistentDto)
        title = guestBook.title
        user = Optional.ofNullable(guestBook.userInternal).map { user: UserInternalDto ->
            UserEntity(user)
        }.orElse(null)
    }

    private fun copyUserWithoutId(): UserEntity? {
        return Optional.ofNullable(user).map { obj: UserEntity -> obj.copyWithoutId() }.orElse(null)
    }

    fun asDto(): GuestBookDto {
        return GuestBookDto(
            persistentDataEmbeddable.asPersistentDto(id),
            entries.stream().map { obj: GuestBookEntryEntity -> obj.asDto() }.collect(Collectors.toSet()),
            title,
            Optional.ofNullable(user).map { obj: UserEntity -> obj.asDto() }.orElse(null)
        )
    }

    override fun copyWithoutId(): GuestBookEntity {
        val guestBookEntity = GuestBookEntity(this)
        guestBookEntity.id = null
        return guestBookEntity
    }

    override fun modifiedBy(modifier: String): GuestBookEntity {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    fun add(guestBookEntry: GuestBookEntryEntity) {
        entries.add(guestBookEntry)
        guestBookEntry.guestBook = this
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other != null && javaClass == other.javaClass &&
                title == (other as GuestBookEntity).title &&
                user == other.user
    }

    override fun hashCode(): Int {
        return Objects.hash(title, user)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .appendSuper(super.toString())
            .append(title)
            .append(user)
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

    fun getEntries(): Set<GuestBookEntryEntity> {
        return entries
    }

    companion object {
        @JvmStatic
        fun aGuestBook(guestBookDto: GuestBookDto): GuestBookEntity {
            return GuestBookEntity(guestBookDto)
        }
    }
}
