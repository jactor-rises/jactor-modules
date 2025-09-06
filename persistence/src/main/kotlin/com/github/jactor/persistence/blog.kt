package com.github.jactor.persistence

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.persistence.Config.ioContext
import com.github.jactor.persistence.common.EntryEmbeddable
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.PersistentEntity
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.whenTrue
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

@RestController
@RequestMapping(value = ["/blog"], produces = [MediaType.APPLICATION_JSON_VALUE])
class BlogController(private val blogService: BlogService) {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "En blogg for id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke blog for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )

    @Operation(description = "Henter en blogg ved å angi id")
    @GetMapping("/{id}")
    suspend operator fun get(@PathVariable("id") blogId: UUID): ResponseEntity<BlogDto> {
        return blogService.find(blogId)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Et blogg-innslag for id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )

    @Operation(description = "Henter et innslag i en blogg ved å angi id")
    @GetMapping("/entry/{id}")
    suspend fun getEntryById(@PathVariable("id") blogEntryId: UUID): ResponseEntity<BlogEntryDto> {
        return blogService.findEntryBy(blogEntryId)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blogger basert på tittel"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )

    @GetMapping("/title/{title}")
    @Operation(description = "Søker etter blogger basert på en blog tittel")
    suspend fun findByTitle(@PathVariable("title") title: String?): ResponseEntity<List<BlogDto>> {
        val blogsByTitle = blogService.findBlogsBy(title)
            .map { it.toDto() }

        return when (blogsByTitle.isNotEmpty()) {
            true -> ResponseEntity(blogsByTitle, HttpStatus.OK)
            false -> ResponseEntity(HttpStatus.NO_CONTENT)
        }
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blogg-innslag basert på blogg id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )

    @GetMapping("/{id}/entries")
    @Operation(description = "Søker etter blogg-innslag basert på en blogg id")
    suspend fun findEntriesByBlogId(@PathVariable("id") blogId: UUID): ResponseEntity<List<BlogEntryDto>> {
        val entriesForBlog = blogService.findEntriesForBlog(blogId)
            .map { it.toDto() }

        return when (entriesForBlog.isNotEmpty()) {
            true -> ResponseEntity(entriesForBlog, HttpStatus.OK)
            false -> ResponseEntity(HttpStatus.NO_CONTENT)
        }
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Bloggen er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Kunnde ikke finne blogg til å endre",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )

    @Operation(description = "Endre en blogg")
    @PutMapping("/{blogId}")
    suspend fun put(
        @RequestBody blogDto: BlogDto, @PathVariable blogId: UUID
    ): ResponseEntity<BlogDto> = when (blogDto.harIkkeIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            blogService.saveOrUpdate(blog = Blog(blogDto = blogDto)).toDto(),
            HttpStatus.ACCEPTED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Bloggen er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Mangler blogg å opprette",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @Operation(description = "Opprett en blogg")
    @PostMapping
    suspend fun post(@RequestBody blogDto: BlogDto): ResponseEntity<BlogDto> = when (blogDto.harIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            blogService.saveOrUpdate(blog = Blog(blogDto)).toDto(),
            HttpStatus.CREATED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Blogg-innslaget er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Mangler id til blogg-innslag som skal endres",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )

    @Operation(description = "Endrer et blogg-innslag")
    @PutMapping("/entry/{blogEntryId}")
    suspend fun putEntry(
        @RequestBody blogEntryDto: BlogEntryDto,
        @PathVariable blogEntryId: UUID
    ): ResponseEntity<BlogEntryDto> = when (blogEntryDto.harIkkeIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            blogService.saveOrUpdate(blogEntry = BlogEntry(blogEntry = blogEntryDto)).toDto(),
            HttpStatus.ACCEPTED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Blogg-innslaget er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Mangler id til bloggen som innsaget skal legges  til",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )

    @Operation(description = "Oppretter et blogg-innslag")
    @PostMapping("/entry")
    suspend fun postEntry(
        @RequestBody blogEntryDto: BlogEntryDto
    ): ResponseEntity<BlogEntryDto> = when (blogEntryDto.harIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> blogService.saveOrUpdate(
            blogEntry = BlogEntry(blogEntry = blogEntryDto)
        ).let {
            ResponseEntity(it.toDto(), HttpStatus.CREATED)
        }
    }
}

interface BlogService {
    suspend fun find(id: UUID): Blog?
    suspend fun findBlogsBy(title: String?): List<Blog>
    suspend fun findEntriesForBlog(blogId: UUID?): List<BlogEntry>
    suspend fun findEntryBy(blogEntryId: UUID): BlogEntry?
    suspend fun saveOrUpdate(blog: Blog): Blog
    suspend fun saveOrUpdate(blogEntry: BlogEntry): BlogEntry
}

@Service
class BlogServiceImpl(
    private val blogRepository: BlogRepository,
    private val blogEntryRepository: BlogEntryRepository,
    private val userService: UserService
) : BlogService {
    override suspend fun find(id: UUID): Blog? = ioContext { blogRepository.findById(id) }
        .map { it.toModel() }
        .orElse(null)

    override suspend fun findEntryBy(blogEntryId: UUID): BlogEntry? = ioContext {
        blogEntryRepository.findById(blogEntryId)
            .map { it.toModel() }
            .orElse(null)
    }

    override suspend fun findBlogsBy(title: String?): List<Blog> = ioContext {
        blogRepository.findBlogsByTitle(title).map { obj: BlogEntity? -> obj?.toModel()!! }
    }

    override suspend fun findEntriesForBlog(blogId: UUID?): List<BlogEntry> = ioContext {
        blogEntryRepository.findByBlogId(blogId).map { obj: BlogEntryEntity? -> obj?.toModel()!! }
    }

    override suspend fun saveOrUpdate(blog: Blog): Blog = ioContext {
        val user = userService.find(username = fetchUsername(blog))
        blogRepository.save(BlogEntity(blog.copy(user = user))).toModel()
    }

    override suspend fun saveOrUpdate(blogEntry: BlogEntry): BlogEntry = ioContext {
        blogEntry.blog?.also { it.id ?: error("An entry must belong to a persistent blog!") }
            ?: error("An entry must belong to a blog!")

        val blogEntryEntity = BlogEntryEntity(blogEntry)

        blogEntryRepository.save(blogEntryEntity).toModel()
    }

    private fun fetchUsername(blog: Blog?): String {
        return blog?.user?.username
            ?: throw IllegalStateException("Unnable to find username in $blog")
    }
}

@JvmRecord
data class Blog(
    val created: LocalDate?,
    val persistent: Persistent = Persistent(),
    val title: String?,
    val user: User?,
) {
    val id: UUID? @JsonIgnore get() = persistent.id

    constructor(blogDto: BlogDto) : this(
        created = null,
        persistent = Persistent(blogDto.persistentDto),
        title = blogDto.title,
        user = blogDto.user?.let { User(userDto = it) }
    )

    constructor(persistent: Persistent, blog: Blog) : this(
        persistent = persistent,
        created = blog.created,
        title = blog.title,
        user = blog.user
    )

    fun toDto() = BlogDto(
        persistentDto = persistent.toDto(),
        title = title,
        user = user?.toDto()
    )

    fun withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
    fun toEntity() = BlogEntity(blog = this)
}

@JvmRecord
data class BlogEntry(
    val blog: Blog?,
    val creatorName: String?,
    val entry: String?,
    val persistent: Persistent = Persistent(),
) {
    val id: UUID? @JsonIgnore get() = persistent.id
    val notNullableEntry: String
        @JsonIgnore get() = entry ?: throw IllegalStateException("An entry is not provided!")
    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("A creator is not provided!")

    constructor(blogEntry: BlogEntryDto) : this(
        persistent = Persistent(blogEntry.persistentDto),
        blog = blogEntry.blogDto?.let { Blog(blogDto = it) },
        creatorName = blogEntry.creatorName,
        entry = blogEntry.entry,
    )

    constructor(persistent: Persistent, blogEntry: BlogEntry) : this(
        persistent = persistent,
        blog = blogEntry.blog,
        creatorName = blogEntry.creatorName,
        entry = blogEntry.entry
    )

    fun toDto() = BlogEntryDto(
        persistentDto = persistent.toDto(),
        blogDto = blog?.toDto(),
        creatorName = creatorName,
        entry = entry,
    )

    fun withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
    fun toEntity() = BlogEntryEntity(blogEntry = this)
}

interface BlogRepository : CrudRepository<BlogEntity, UUID> {
    fun findBlogsByTitle(title: String?): List<BlogEntity>
}

interface BlogEntryRepository : CrudRepository<BlogEntryEntity, UUID> {
    fun findByBlogId(blogId: UUID?): List<BlogEntryEntity?>
}

@Entity
@Table(name = "T_BLOG")
class BlogEntity : PersistentEntity<BlogEntity?> {
    @Id
    override var id: UUID? = null

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

    constructor(blog: Blog) {
        created = blog.created
        id = blog.id
        persistentDataEmbeddable = PersistentDataEmbeddable(blog.persistent)
        title = blog.title
        user = blog.user?.let { UserEntity(user = it) }
    }

    fun toModel(): Blog {
        return Blog(
            created = created,
            persistent = persistentDataEmbeddable.toModel(id),
            title = title,
            user = user?.toModel()
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
}

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

    constructor(blogEntry: BlogEntry) {
        blog = BlogEntity(blog = blogEntry.blog ?: error("Entry must belong to a blog"))
        entryEmbeddable = EntryEmbeddable(blogEntry.notNullableCreator, blogEntry.notNullableEntry)
        id = blogEntry.id
        persistentDataEmbeddable = PersistentDataEmbeddable(blogEntry.persistent)
    }

    private fun copyBlog(): BlogEntity {
        return blog?.copyWithoutId() ?: throw IllegalStateException("No blog to copy!")
    }

    private fun copyEntry(): EntryEmbeddable {
        return entryEmbeddable.copy()
    }

    fun toModel() = BlogEntry(
        blog = blog?.toModel(),
        creatorName = entryEmbeddable.creatorName,
        entry = entryEmbeddable.entry,
        persistent = Persistent(id = id)
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
