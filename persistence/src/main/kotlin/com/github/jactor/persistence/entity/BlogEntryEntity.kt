package com.github.jactor.persistence.entity

import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import com.github.jactor.persistence.dto.BlogEntryModel
import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "T_BLOG_ENTRY")
class BlogEntryEntity : PersistentEntity<BlogEntryEntity?> {
    @Id
    override var id: UUID? = null

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

    constructor(blogEntryModel: BlogEntryModel) {
        blog = BlogEntity(blogModel = blogEntryModel.blog ?: error("Entry must belong to a blog"))
        entryEmbeddable = EntryEmbeddable(blogEntryModel.notNullableCreator, blogEntryModel.notNullableEntry)
        id = blogEntryModel.id
        persistentDataEmbeddable = PersistentDataEmbeddable(blogEntryModel.persistentModel)
    }

    private fun copyBlog(): BlogEntity {
        return blog?.copyWithoutId() ?: throw IllegalStateException("No blog to copy!")
    }

    private fun copyEntry(): EntryEmbeddable {
        return entryEmbeddable.copy()
    }

    fun toModel()= BlogEntryModel (
        blog = blog?.let { it.toModel() },
        creatorName = entryEmbeddable.creatorName,
        entry = entryEmbeddable.entry
    )

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
}
