package com.github.jactor.persistence.entity

import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.time.LocalDateTime
import java.util.Objects
import java.util.Optional

@Entity
@Table(name = "T_GUEST_BOOK_ENTRY")
class GuestBookEntryEntity : PersistentEntity<GuestBookEntryEntity?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guestBookEntrySeq")
    @SequenceGenerator(name = "guestBookEntrySeq", sequenceName = "T_GUEST_BOOK_ENTRY_SEQ", allocationSize = 1)
    override var id: Long? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    private lateinit var persistentDataEmbeddable: PersistentDataEmbeddable

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "GUEST_BOOK_ID")
    var guestBook: GuestBookEntity? = null

    @Embedded
    @AttributeOverride(name = "creatorName", column = Column(name = "GUEST_NAME"))
    @AttributeOverride(name = "entry", column = Column(name = "ENTRY"))
    private var entryEmbeddable = EntryEmbeddable()

    constructor() {
        // used by entity manager
    }

    private constructor(guestBookEntry: GuestBookEntryEntity) {
        entryEmbeddable = guestBookEntry.copyEntry()
        guestBook = guestBookEntry.copyGuestBookWithoutId()
        id = guestBookEntry.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
    }

    constructor(guestBookEntry: GuestBookEntryDto) {
        entryEmbeddable = EntryEmbeddable(guestBookEntry.notNullableCreator, guestBookEntry.notNullableEntry)
        guestBook = Optional.ofNullable(guestBookEntry.guestBook).map { guestBook: GuestBookDto? ->
            GuestBookEntity(
                guestBook!!
            )
        }.orElse(null)
        id = guestBookEntry.id
        persistentDataEmbeddable = PersistentDataEmbeddable(guestBookEntry.persistentDto)
    }

    private fun copyGuestBookWithoutId(): GuestBookEntity {
        return guestBook!!.copyWithoutId()
    }

    private fun copyEntry(): EntryEmbeddable {
        return entryEmbeddable.copy()
    }

    fun asDto(): GuestBookEntryDto {
        return asDto(guestBook!!.asDto())
    }

    private fun asDto(guestBook: GuestBookDto): GuestBookEntryDto {
        return GuestBookEntryDto(
            persistentDataEmbeddable.asPersistentDto(id),
            guestBook,
            entryEmbeddable.creatorName,
            entryEmbeddable.entry
        )
    }

    fun modify(modifiedBy: String, entry: String) {
        entryEmbeddable.modify(modifiedBy, entry)
        persistentDataEmbeddable.modifiedBy(modifiedBy)
    }

    override fun copyWithoutId(): GuestBookEntryEntity {
        val guestBookEntryEntity = GuestBookEntryEntity(this)
        guestBookEntryEntity.id = null
        return guestBookEntryEntity
    }

    override fun modifiedBy(modifier: String): GuestBookEntryEntity {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other != null && javaClass == other.javaClass && isEqualTo(other as GuestBookEntryEntity)
    }

    private fun isEqualTo(o: GuestBookEntryEntity): Boolean {
        return entryEmbeddable == o.entryEmbeddable &&
                guestBook == o.guestBook
    }

    override fun hashCode(): Int {
        return Objects.hash(guestBook, entryEmbeddable)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .appendSuper(super.toString())
            .append(guestBook)
            .append(entryEmbeddable)
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
    val entry: String
        get() = entryEmbeddable.notNullableEntry
    val creatorName: String
        get() = entryEmbeddable.notNullableCreator

    companion object {
        @JvmStatic
        fun aGuestBookEntry(guestBookEntryDto: GuestBookEntryDto): GuestBookEntryEntity {
            return GuestBookEntryEntity(guestBookEntryDto)
        }
    }
}
