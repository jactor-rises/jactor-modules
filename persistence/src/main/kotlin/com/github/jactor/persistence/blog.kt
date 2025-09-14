package com.github.jactor.persistence

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.date
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertIgnoreAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.github.jactor.persistence.common.EntryDao
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

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
        return blogService.findEntryBy(blogEntryId)?.let { ResponseEntity(it.toBlogEntryDto(), HttpStatus.OK) }
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
    suspend fun findByTitle(@PathVariable("title") title: String): ResponseEntity<List<BlogDto>> {
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
            .map { it.toBlogEntryDto() }

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
            blogService.saveOrUpdate(blogEntry = BlogEntry(blogEntry = blogEntryDto)).toBlogEntryDto(),
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
            ResponseEntity(it.toBlogEntryDto(), HttpStatus.CREATED)
        }
    }
}

interface BlogService {
    suspend fun find(id: UUID): Blog?
    suspend fun findBlogsBy(title: String): List<Blog>
    suspend fun findEntriesForBlog(blogId: UUID): List<BlogEntry>
    suspend fun findEntryBy(blogEntryId: UUID): BlogEntry?
    suspend fun saveOrUpdate(blog: Blog): Blog
    suspend fun saveOrUpdate(blogEntry: BlogEntry): BlogEntry
}

@Service
class BlogServiceImpl(
    private val blogRepository: BlogRepository,
    private val userService: UserService
) : BlogService {
    override suspend fun find(id: UUID): Blog? = blogRepository.findBlogById(id)?.toBlog()
    override suspend fun findEntryBy(blogEntryId: UUID): BlogEntry? {
        return blogRepository.findBlogEntryById(blogEntryId)?.toBlogEntry()
    }

    override suspend fun findBlogsBy(title: String): List<Blog> {
        return blogRepository.findBlogsByTitle(title).map { it.toBlog() }
    }

    override suspend fun findEntriesForBlog(blogId: UUID): List<BlogEntry> {
        return blogRepository.findBlogEntriesByBlogId(blogId).map { it.toBlogEntry() }
    }

    override suspend fun saveOrUpdate(blog: Blog): Blog {
        val user = userService.find(username = blog.fetchUsername())
        return blogRepository.insertOrUpdate(BlogDao(blog.copy(user = user))).toBlog()
    }

    override suspend fun saveOrUpdate(blogEntry: BlogEntry): BlogEntry {
        require(blogEntry.isCoupledWithBlog) { "An entry must belong to a persistent blog!" }
        val blogEntryDao = BlogEntryDao(blogEntry)

        return blogRepository.insertOrUpdate(blogEntryDao).toBlogEntry()
    }
}

@JvmRecord
data class Blog(
    internal val persistent: Persistent = Persistent(),

    val created: LocalDate?,
    val title: String?,
    val user: User?,
) {
    constructor(blogDto: BlogDto) : this(
        persistent = blogDto.persistentDto.toPersistent(),
        created = blogDto.persistentDto.timeOfCreation?.toLocalDate() ?: LocalDate.now(),
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
        persistentDto = persistent.toPersistentDto(),
        title = title,
        user = user?.toDto()
    )

    fun fetchUsername(): String = this.user?.username ?: error("Unnable to find username in $this")
    fun toDao() = BlogDao(blog = this)
    fun withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
}

@JvmRecord
data class BlogEntry(
    val blog: Blog?,
    val creatorName: String?,
    val entry: String?,
    val persistent: Persistent = Persistent(),
) {
    constructor(blogEntry: BlogEntryDto) : this(
        persistent = blogEntry.persistentDto.toPersistent(),

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

    fun toBlogEntryDto() = BlogEntryDto(
        persistentDto = persistent.toPersistentDto(),
        blogDto = blog?.toDto(),
        creatorName = creatorName,
        entry = entry,
    )

    val isCoupledWithBlog: Boolean
        get() = blog?.persistent?.id != null

    fun withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
    fun toBlogEntryDao() = BlogEntryDao(blogEntry = this)
}

object Blogs : UUIDTable(name = "T_BLOG", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val created = date("CREATED")
    val title = text("TITLE").nullable()
    val userId = uuid("USER_ID").references(Users.id)
}

object BlogEntries : UUIDTable(name = "T_BLOG_ENTRY", columnName = "ID") {
    val blogId = uuid("BLOG_ID").references(Blogs.id)
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val creatorName = text("CREATOR_NAME")
    val entry = text("ENTRY")
}

@Repository
class BlogRepository {
    fun findBlogById(id: UUID): BlogDao? = transaction {
        Blogs.selectAll()
            .andWhere { Blogs.id eq id }
            .map { row -> row.toBlogDao() }
            .singleOrNull()
    }

    fun findBlogEntryById(id: UUID): BlogEntryDao? = transaction {
        BlogEntries.selectAll()
            .andWhere { BlogEntries.id eq id }
            .map { row -> row.toBlogEntryDao() }
            .singleOrNull()
    }

    fun findBlogsByTitle(title: String): List<BlogDao> = transaction {
        Blogs.selectAll()
            .andWhere { Blogs.title eq title }
            .map { row -> row.toBlogDao() }
    }

    fun findBlogEntriesByBlogId(blogId: UUID?): List<BlogEntryDao> = transaction {
        (BlogEntries innerJoin Blogs)
            .selectAll()
            .andWhere { Blogs.id eq blogId }
            .map { row -> row.toBlogEntryDao() }
    }

    fun insertOrUpdate(blogDao: BlogDao): BlogDao {
        return blogDao.id?.let { id ->
            Blogs.update(where = { Blogs.id eq id }) { update ->
                update[modifiedBy] = blogDao.modifiedBy
                update[timeOfModification] = blogDao.timeOfModification
                update[created] = blogDao.created ?: blogDao.timeOfCreation.toLocalDate()
                update[title] = blogDao.title
                update[userId] = blogDao.user?.id ?: error("A blog must belong to a user")
            }

            blogDao
        } ?: run {
            val id = Blogs.insertIgnoreAndGetId {
                it[created] = blogDao.created ?: blogDao.timeOfCreation.toLocalDate()
                it[createdBy] = blogDao.createdBy
                it[modifiedBy] = blogDao.modifiedBy
                it[timeOfCreation] = blogDao.timeOfCreation
                it[timeOfModification] = blogDao.timeOfModification
                it[title] = blogDao.title
                it[userId] = blogDao.user?.id ?: error("A blog must belong to a user")
            }?.value

            blogDao.copy(id = id)
        }
    }

    fun insertOrUpdate(blogEntryDao: BlogEntryDao): BlogEntryDao {
        return blogEntryDao.id?.let {
            BlogEntries.update(where = { BlogEntries.id eq it }) {
                it[blogId] = blogEntryDao.blogDao?.id ?: error("A blog entry must belong to a blog")
                it[createdBy] = blogEntryDao.createdBy
                it[creatorName] = blogEntryDao.creatorName
                it[modifiedBy] = blogEntryDao.modifiedBy
                it[timeOfCreation] = blogEntryDao.timeOfCreation
                it[timeOfModification] = blogEntryDao.timeOfModification
            }

            blogEntryDao
        } ?: run {
            val id = BlogEntries.insertIgnoreAndGetId {
                it[blogId] = blogEntryDao.blogDao?.id ?: error("A blog entry must belong to a blog")
                it[createdBy] = blogEntryDao.createdBy
                it[creatorName] = blogEntryDao.creatorName
                it[entry] = blogEntryDao.entry
                it[modifiedBy] = blogEntryDao.modifiedBy
                it[timeOfCreation] = blogEntryDao.timeOfCreation
                it[timeOfModification] = blogEntryDao.timeOfModification
            }?.value

            blogEntryDao.copy(id = id)
        }
    }
}

private fun ResultRow.toBlogDao(): BlogDao = BlogDao(
    id = this[Blogs.id].value,
    created = this[Blogs.created],
    createdBy = this[Blogs.createdBy],
    modifiedBy = this[Blogs.modifiedBy],
    timeOfCreation = this[Blogs.timeOfCreation],
    timeOfModification = this[Blogs.timeOfModification],
    title = this[Blogs.title],
    user = this.toUserDao(),
)

private fun ResultRow.toBlogEntryDao(): BlogEntryDao = BlogEntryDao(
    id = this[BlogEntries.id].value,
    createdBy = this[BlogEntries.createdBy],
    timeOfCreation = this[BlogEntries.timeOfCreation],
    modifiedBy = this[BlogEntries.modifiedBy],
    timeOfModification = this[BlogEntries.timeOfModification],
    creatorName = this[BlogEntries.creatorName],
    entry = this[BlogEntries.entry],
    blogDao = this.toBlogDao()
)

data class BlogDao(
    override var id: UUID? = null,
    override var createdBy: String = "todo",
    override var timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var modifiedBy: String = "todo",
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    var created: LocalDate? = null,
    var title: String? = null,
    var user: UserDao? = null,
    var entries: MutableSet<BlogEntryDao> = HashSet()
) : PersistentDao<BlogDao> {
    constructor(blog: Blog) : this(
        id = blog.persistent.id,
        created = blog.created,
        createdBy = blog.persistent.createdBy,
        entries = mutableSetOf(),
        modifiedBy = blog.persistent.modifiedBy,
        timeOfCreation = blog.persistent.timeOfCreation,
        timeOfModification = blog.persistent.timeOfModification,
        title = blog.title,
        user = blog.user?.let { UserDao(it) },
    )

    override fun copyWithoutId(): BlogDao = copy(id = null)
    override fun modifiedBy(modifier: String): BlogDao = copy(
        modifiedBy = modifier,
        timeOfModification = LocalDateTime.now()
    )

    fun add(blogEntryDao: BlogEntryDao) {
        blogEntryDao.blogDao = this
        entries.add(blogEntryDao)
    }

    fun toBlog(): Blog = Blog(
        created = created,
        persistent = toPersistent(),
        title = title,
        user = user?.toUser()
    )
}

data class BlogEntryDao(
    override var id: UUID?,
    override val createdBy: String,
    override var creatorName: String,
    override var entry: String,
    override var modifiedBy: String,
    override val timeOfCreation: LocalDateTime,
    override var timeOfModification: LocalDateTime,

    var blogDao: BlogDao? = null
) : PersistentDao<BlogEntryDao>, EntryDao {
    constructor(blogEntry: BlogEntry) : this(
        id = blogEntry.persistent.id,

        blogDao = blogEntry.blog?.let { BlogDao(blog = it) } ?: error("Entry must belong to a blog"),
        createdBy = blogEntry.persistent.createdBy,
        creatorName = blogEntry.creatorName ?: throw IllegalArgumentException("Creator name must not be null"),
        entry = blogEntry.entry ?: throw IllegalArgumentException("Entry must not be null"),
        timeOfCreation = blogEntry.persistent.timeOfCreation,
        modifiedBy = blogEntry.persistent.modifiedBy,
        timeOfModification = blogEntry.persistent.timeOfModification,
    )

    override fun copyWithoutId(): BlogEntryDao = copy(id = null)
    override fun modifiedBy(modifier: String): BlogEntryDao = copy(
        modifiedBy = modifier,
        timeOfModification = LocalDateTime.now()
    )

    fun toBlogEntry() = BlogEntry(
        persistent = toPersistent(),

        blog = blogDao?.toBlog(),
        creatorName = creatorName,
        entry = entry,
    )
}
