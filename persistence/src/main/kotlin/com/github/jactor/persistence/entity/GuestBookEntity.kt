package com.github.jactor.persistence.entity

import java.time.LocalDateTime
import java.util.Objects
import java.util.stream.Collectors
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
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
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

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
        entries = guestBook.entries.stream().map { obj: GuestBookEntryEntity -> obj.copyWithoutId() }
            .collect(Collectors.toSet())
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
        user = guestBook.userInternal?.let { UserEntity(it) }
    }

    private fun copyUserWithoutId(): UserEntity? {
        return user?.copyWithoutId()
    }

    fun asDto(): GuestBookDto {
        return GuestBookDto(
            persistentDataEmbeddable.asPersistentDto(id),
            entries.stream().map { obj: GuestBookEntryEntity -> obj.asDto() }.collect(Collectors.toSet()),
            title,
            user?.asDto()
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
