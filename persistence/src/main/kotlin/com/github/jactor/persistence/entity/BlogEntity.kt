package com.github.jactor.persistence.entity

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Objects
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import com.github.jactor.persistence.dto.BlogDto
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

@Entity
@Table(name = "T_BLOG")
class BlogEntity : PersistentEntity<BlogEntity?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogSeq")
    @SequenceGenerator(name = "blogSeq", sequenceName = "T_BLOG_SEQ", allocationSize = 1)
    override var id: Long? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    private lateinit var persistentDataEmbeddable: PersistentDataEmbeddable

    @Column(name = "CREATED")
    var created: LocalDate? = null
        private set

    @Column(name = "TITLE")
    var title: String? = null

    @JoinColumn(name = "USER_ID")
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    var user: UserEntity? = null

    @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    private var entries: MutableSet<BlogEntryEntity> = HashSet()

    constructor() {
        // used by entity manager
    }

    private constructor(blogEntity: BlogEntity) {
        created = blogEntity.created
        entries = blogEntity.entries
            .map { obj: BlogEntryEntity -> obj.copyWithoutId() }
            .toMutableSet()
        id = blogEntity.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        title = blogEntity.title
        user = blogEntity.user?.copyWithoutId()
    }

    constructor(blogDto: BlogDto) {
        created = blogDto.created
        id = blogDto.id
        persistentDataEmbeddable = PersistentDataEmbeddable(blogDto.persistentDto)
        title = blogDto.title
        user = blogDto.userInternal?.let { UserEntity(it) }
    }

    fun asDto(): BlogDto {
        return BlogDto(
            persistentDataEmbeddable.asPersistentDto(id), created, title, user?.asDto()
        )
    }

    fun add(blogEntryEntity: BlogEntryEntity) {
        blogEntryEntity.blog = this
        entries.add(blogEntryEntity)
    }

    override fun copyWithoutId(): BlogEntity {
        val blogEntity = BlogEntity(this)
        blogEntity.id = null
        return blogEntity
    }

    override fun modifiedBy(modifier: String): BlogEntity {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other != null && javaClass == other.javaClass &&
            title == (other as BlogEntity).title &&
            user == other.user
    }

    override fun hashCode(): Int {
        return Objects.hash(title, user)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .appendSuper(super.toString())
            .append(created)
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

    fun getEntries(): Set<BlogEntryEntity> {
        return entries
    }

    companion object {
        @JvmStatic
        fun aBlog(blogDto: BlogDto): BlogEntity {
            return BlogEntity(blogDto)
        }
    }
}
