package com.github.jactor.persistence.entity

import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import javax.persistence.AttributeOverride
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.time.LocalDateTime
import java.util.Objects
import java.util.Optional

@Entity
@Table(name = "T_BLOG_ENTRY")
class BlogEntryEntity : PersistentEntity<BlogEntryEntity?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogEntrySeq")
    @SequenceGenerator(name = "blogEntrySeq", sequenceName = "T_BLOG_ENTRY_SEQ", allocationSize = 1)
    override var id: Long? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    private lateinit var persistentDataEmbeddable: PersistentDataEmbeddable

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "BLOG_ID")
    var blog: BlogEntity? = null

    @Embedded
    @AttributeOverride(name = "creatorName", column = Column(name = "CREATOR_NAME"))
    @AttributeOverride(name = "entry", column = Column(name = "ENTRY"))
    private var entryEmbeddable = EntryEmbeddable()

    constructor() {
        // used by entity manager
    }

    private constructor(blogEntryEntity: BlogEntryEntity) {
        blog = blogEntryEntity.copyBlog()
        entryEmbeddable = blogEntryEntity.copyEntry()
        id = blogEntryEntity.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
    }

    constructor(blogEntryDto: BlogEntryDto) {
        blog = Optional.ofNullable(blogEntryDto.blog).map { blogDto: BlogDto? ->
            BlogEntity(
                blogDto!!
            )
        }.orElse(null)
        entryEmbeddable = EntryEmbeddable(blogEntryDto.notNullableCreator, blogEntryDto.notNullableEntry)
        id = blogEntryDto.id
        persistentDataEmbeddable = PersistentDataEmbeddable(blogEntryDto.persistentDto)
    }

    private fun copyBlog(): BlogEntity {
        return blog?.copyWithoutId() ?: throw IllegalStateException("No blog to copy!")
    }

    private fun copyEntry(): EntryEmbeddable {
        return entryEmbeddable.copy()
    }

    fun asDto(): BlogEntryDto {
        return asDto(blog!!.asDto())
    }

    private fun asDto(blogDto: BlogDto): BlogEntryDto {
        val blogEntryDto = BlogEntryDto()
        blogEntryDto.blog = blogDto
        blogEntryDto.creatorName = entryEmbeddable.creatorName
        blogEntryDto.entry = entryEmbeddable.entry
        return blogEntryDto
    }

    fun modify(entry: String, modifiedCreator: String) {
        entryEmbeddable.modify(modifiedCreator, entry)
        persistentDataEmbeddable.modifiedBy(modifiedCreator)
    }

    override fun copyWithoutId(): BlogEntryEntity {
        val blogEntryEntity = BlogEntryEntity(this)
        blogEntryEntity.id = null
        return blogEntryEntity
    }

    override fun modifiedBy(modifier: String): BlogEntryEntity {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other != null && javaClass == other.javaClass && isEqualTo(other as BlogEntryEntity)
    }

    private fun isEqualTo(blogEntry: BlogEntryEntity): Boolean {
        return entryEmbeddable == blogEntry.entryEmbeddable &&
                blog == blogEntry.blog
    }

    override fun hashCode(): Int {
        return Objects.hash(blog, entryEmbeddable)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .appendSuper(super.toString())
            .append(blog)
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
    val creatorName: String
        get() = entryEmbeddable.notNullableCreator
    val entry: String
        get() = entryEmbeddable.notNullableEntry

    companion object {
        @JvmStatic
        fun aBlogEntry(blogEntryDto: BlogEntryDto): BlogEntryEntity {
            return BlogEntryEntity(blogEntryDto)
        }
    }
}
