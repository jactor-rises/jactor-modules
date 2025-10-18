package com.github.jactor.persistence

import com.github.jactor.persistence.common.*
import com.github.jactor.persistence.util.toBlog
import com.github.jactor.persistence.util.toBlogEntry
import com.github.jactor.persistence.util.toCreateBlogEntry
import com.github.jactor.persistence.util.toUpdateBlogTitle
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.CreateBlogEntryCommand
import com.github.jactor.shared.api.UpdateBlogTitleCommand
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.date
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping(value = ["/blog"], produces = [MediaType.APPLICATION_JSON_VALUE])
class BlogController(private val blogService: BlogService) {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "En blogg for id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke blog for id",
            )
        ]
    )

    @Operation(description = "Henter en blogg ved å angi id")
    @GetMapping("/{id}")
    suspend operator fun get(@PathVariable("id") blogId: UUID): ResponseEntity<BlogDto> {
        return blogService.find(blogId)?.let { ResponseEntity(it.toBlogDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Et blogg-innslag for id"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ikke innslaget for id",
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
            )
        ]
    )

    @GetMapping("/title/{title}")
    @Operation(description = "Søker etter blogger basert på en blog tittel")
    suspend fun findByTitle(@PathVariable("title") title: String): ResponseEntity<List<BlogDto>> {
        val blogsByTitle = blogService.findBlogsBy(title)
            .map { it.toBlogDto() }

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
            )
        ]
    )

    @Operation(description = "Endre en blogg")
    @PutMapping("/{blogId}")
    suspend fun put(
        @RequestBody updateBlogTitleCommand: UpdateBlogTitleCommand, @PathVariable blogId: UUID
    ): ResponseEntity<BlogDto> = (updateBlogTitleCommand.blogId ?: blogId).let {
        ResponseEntity(
            blogService.update(updateBlogTitle = updateBlogTitleCommand.toUpdateBlogTitle()).toBlogDto(),
            HttpStatus.ACCEPTED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Bloggen er opprettet"),
            ApiResponse(responseCode = "400", description = "Mangler blogg å opprette"),
            ApiResponse(responseCode = "400", description = "Har allerede id på blogg som opprettes"),
        ]
    )
    @Operation(description = "Opprett en blogg")
    @PostMapping
    suspend fun post(@RequestBody blogDto: BlogDto): ResponseEntity<BlogDto> = when (blogDto.harIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            blogService.saveOrUpdate(blog = blogDto.toBlog()).toBlogDto(),
            HttpStatus.CREATED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Blogg-innslaget er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Mangler id til blogg-innslag som skal endres",
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
            blogService.saveOrUpdate(blogEntry = blogEntryDto.toBlogEntry()).toBlogEntryDto(),
            HttpStatus.ACCEPTED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Blogg-innslaget er opprettet"),
            ApiResponse(responseCode = "400", description = "Mangler id til bloggen som innslaget skal legges til"),
            ApiResponse(responseCode = "400", description = "Mangler navn til forfatter av innslag"),
            ApiResponse(responseCode = "400", description = "Mangler innslaget som skal legges inn"),
        ]
    )
    @Operation(description = "Oppretter et blogg-innslag")
    @PostMapping("/entry")
    suspend fun postEntry(
        @RequestBody createBlogEntryCommand: CreateBlogEntryCommand
    ): ResponseEntity<BlogEntryDto> = createBlogEntryCommand.toCreateBlogEntry().let {
        blogService.create(createBlogEntry = it)
    }.toBlogEntryDto().let {
        ResponseEntity(it, HttpStatus.CREATED)
    }
}

interface BlogService {
    suspend fun create(createBlogEntry: CreateBlogEntry): BlogEntry
    suspend fun find(id: UUID): Blog?
    suspend fun findBlogsBy(title: String): List<Blog>
    suspend fun findEntriesForBlog(blogId: UUID): List<BlogEntry>
    suspend fun findEntryBy(blogEntryId: UUID): BlogEntry?
    suspend fun saveOrUpdate(blog: Blog): Blog
    suspend fun saveOrUpdate(blogEntry: BlogEntry): BlogEntry
    suspend fun update(updateBlogTitle: UpdateBlogTitle): Blog
}

@Service
class BlogServiceImpl(private val blogRepository: BlogRepository) : BlogService {
    override suspend fun create(createBlogEntry: CreateBlogEntry): BlogEntry {
        val blogEntryDao = BlogEntryDao(
            id = null,
            createdBy = createBlogEntry.creatorName,
            creatorName = createBlogEntry.creatorName,
            blogId = createBlogEntry.blogId,
            entry = createBlogEntry.entry,
            modifiedBy = createBlogEntry.creatorName,
            timeOfCreation = LocalDateTime.now(),
            timeOfModification = LocalDateTime.now(),
        )

        return blogRepository.save(blogEntryDao = blogEntryDao).toBlogEntry()
    }

    override suspend fun find(id: UUID): Blog? = blogRepository.findBlogById(id)?.toBlog()
    override suspend fun findEntryBy(blogEntryId: UUID): BlogEntry? {
        return blogRepository.findBlogEntryById(blogEntryId)?.toBlogEntry()
    }

    override suspend fun findBlogsBy(title: String): List<Blog> = blogRepository.findBlogsByTitle(title)
        .map { it.toBlog() }

    override suspend fun findEntriesForBlog(blogId: UUID): List<BlogEntry> {
        return blogRepository.findBlogEntriesByBlogId(blogId).map { it.toBlogEntry() }
    }

    override suspend fun saveOrUpdate(blog: Blog): Blog = blogRepository.save(blog.toBlogDao()).toBlog()
    override suspend fun saveOrUpdate(blogEntry: BlogEntry): BlogEntry {
        require(blogEntry.isBlogPersisted) { "An entry must belong to a persistent blog!" }
        return blogRepository.save(blogEntryDao = blogEntry.toBlogEntryDao()).toBlogEntry()
    }

    override suspend fun update(updateBlogTitle: UpdateBlogTitle): Blog {
        val blog = requireNotNull(blogRepository.findBlogById(updateBlogTitle.blogId)) { "Cannot find blog to update" }
            .apply { this.title = updateBlogTitle.title }

        return blogRepository.save(blogDao = blog).toBlog()
    }
}

@JvmRecord
data class Blog(
    internal val persistent: Persistent = Persistent(),

    val created: LocalDate?,
    val title: String,
    val user: User?,
) {
    val id: UUID? get() = persistent.id

    fun toBlogDao() = BlogDao(
        id = persistent.id,
        created = created ?: persistent.timeOfCreation.toLocalDate(),
        createdBy = persistent.createdBy,
        modifiedBy = persistent.modifiedBy,
        timeOfCreation = persistent.timeOfCreation,
        timeOfModification = persistent.timeOfModification,
        title = title,
        userId = user?.persistent?.id,
    )

    fun toBlogDto() = BlogDto(
        persistentDto = persistent.toPersistentDto(),
        title = title,
        user = user?.toUserDto()
    )
}

@JvmRecord
data class BlogEntry(
    val blog: Blog,
    val creatorName: String,
    val entry: String,
    val persistent: Persistent = Persistent(),
) {
    val id: UUID? get() = persistent.id
    val isBlogPersisted: Boolean
        get() = blog.persistent.id != null

    fun toBlogEntryDto() = BlogEntryDto(
        persistentDto = persistent.toPersistentDto(),
        blogDto = blog.toBlogDto(),
        creatorName = creatorName,
        entry = entry,
    )

    fun toBlogEntryDao() = BlogEntryDao(
        id = persistent.id,

        blogId = blog.persistent.id ?: error("A blog entry must belong to a persisted blog!"),
        createdBy = persistent.createdBy,
        creatorName = creatorName,
        entry = entry,
        timeOfCreation = persistent.timeOfCreation,
        modifiedBy = persistent.modifiedBy,
        timeOfModification = persistent.timeOfModification,
    )
}

@JvmRecord
data class CreateBlogEntry(
    val blogId: UUID,
    val creatorName: String,
    val entry: String,
)

@JvmRecord
data class UpdateBlogTitle(
    val blogId: UUID,
    val title: String,
)

object Blogs : UUIDTable(name = "T_BLOG", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val created = date("CREATED")
    val title = text("TITLE")
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

interface BlogRepository {
    fun findBlogById(id: UUID): BlogDao?
    fun findBlogsByUserId(id: UUID): List<BlogDao>
    fun findBlogEntries(): List<BlogEntryDao>
    fun findBlogEntryById(id: UUID): BlogEntryDao?
    fun findBlogs(): List<BlogDao>
    fun findBlogsByTitle(title: String): List<BlogDao>
    fun findBlogEntriesByBlogId(id: UUID): List<BlogEntryDao>
    fun save(blogDao: BlogDao): BlogDao
    fun save(blogEntryDao: BlogEntryDao): BlogEntryDao
}

@Repository
class BlogRepositoryImpl : BlogRepository by BlogRepositoryObject

object BlogRepositoryObject : BlogRepository {
    override fun findBlogById(id: UUID): BlogDao? = transaction {
        Blogs.selectAll()
            .andWhere { Blogs.id eq id }
            .map { it.toBlogDao() }
            .singleOrNull()
    }

    override fun findBlogsByUserId(id: UUID): List<BlogDao> = transaction {
        Blogs.selectAll()
            .andWhere { Blogs.userId eq id }
            .map { it.toBlogDao() }
    }

    override fun findBlogEntries(): List<BlogEntryDao> = transaction {
        BlogEntries.selectAll()
            .map { it.toBlogEntryDao() }
    }

    override fun findBlogEntryById(id: UUID): BlogEntryDao? = transaction {
        BlogEntries.selectAll()
            .andWhere { BlogEntries.id eq id }
            .map { it.toBlogEntryDao() }
            .singleOrNull()
    }

    override fun findBlogs(): List<BlogDao> = transaction {
        Blogs.selectAll()
            .map { it.toBlogDao() }
    }

    override fun findBlogsByTitle(title: String): List<BlogDao> = transaction {
        Blogs.selectAll()
            .andWhere { Blogs.title eq title }
            .map { it.toBlogDao() }
    }

    override fun findBlogEntriesByBlogId(id: UUID): List<BlogEntryDao> = transaction {
        BlogEntries.selectAll()
            .andWhere { BlogEntries.blogId eq id }
            .map { it.toBlogEntryDao() }
    }

    override fun save(blogDao: BlogDao): BlogDao = transaction {
        when (blogDao.isNotPersisted) {
            true -> insert(blogDao)
            false -> update(blogDao)
        }
    }

    private fun insert(blogDao: BlogDao): BlogDao = Blogs.insertAndGetId {
        it[Blogs.created] = blogDao.created
        it[Blogs.createdBy] = blogDao.createdBy
        it[Blogs.modifiedBy] = blogDao.modifiedBy
        it[Blogs.timeOfCreation] = blogDao.timeOfCreation
        it[Blogs.timeOfModification] = blogDao.timeOfModification
        it[Blogs.title] = blogDao.title
        it[Blogs.userId] = requireNotNull(blogDao.userId) { "A blog must belong to a user" }
    }?.value?.let { blogDao.copy(id = it) } ?: error("Unable to insert BlogDao: $blogDao")

    private fun update(blogDao: BlogDao): BlogDao = Blogs.update(
        where = { Blogs.id eq blogDao.id }
    ) { update ->
        update[Blogs.modifiedBy] = blogDao.modifiedBy
        update[Blogs.timeOfModification] = blogDao.timeOfModification
        update[Blogs.created] = blogDao.created
        update[Blogs.title] = blogDao.title
        update[Blogs.userId] = requireNotNull(blogDao.userId) { "A blog must belong to a user" }
    }.let { blogDao }

    override fun save(blogEntryDao: BlogEntryDao): BlogEntryDao = transaction {
        when (blogEntryDao.isNotPersisted) {
            true -> insert(blogEntryDao)
            false -> update(blogEntryDao)
        }
    }

    private fun insert(blogEntryDao: BlogEntryDao): BlogEntryDao = BlogEntries.insertAndGetId { insert ->
        insert[blogId] = requireNotNull(blogEntryDao.blogId) { "A blog entry must belong to a blog" }
        insert[createdBy] = blogEntryDao.createdBy
        insert[creatorName] = blogEntryDao.creatorName
        insert[entry] = blogEntryDao.entry
        insert[modifiedBy] = blogEntryDao.modifiedBy
        insert[timeOfCreation] = blogEntryDao.timeOfCreation
        insert[timeOfModification] = blogEntryDao.timeOfModification
    }?.value?.let { blogEntryDao.copy(id = it) } ?: error("Unable to insert BlogEntryDao: $blogEntryDao")

    private fun update(blogEntryDao: BlogEntryDao): BlogEntryDao = BlogEntries.update(
        where = { BlogEntries.id eq blogEntryDao.id },
    ) { update ->
        update[blogId] = requireNotNull(blogEntryDao.blogId) { "A blog entry must belong to a blog" }
        update[createdBy] = blogEntryDao.createdBy
        update[creatorName] = blogEntryDao.creatorName
        update[modifiedBy] = blogEntryDao.modifiedBy
        update[timeOfCreation] = blogEntryDao.timeOfCreation
        update[timeOfModification] = blogEntryDao.timeOfModification
    }.let { blogEntryDao }

    private fun ResultRow.toBlogDao(): BlogDao = BlogDao(
        id = this[Blogs.id].value,
        created = this[Blogs.created],
        createdBy = this[Blogs.createdBy],
        modifiedBy = this[Blogs.modifiedBy],
        timeOfCreation = this[Blogs.timeOfCreation],
        timeOfModification = this[Blogs.timeOfModification],
        title = this[Blogs.title],
        userId = this[Blogs.userId],
    )

    private fun ResultRow.toBlogEntryDao(): BlogEntryDao = BlogEntryDao(
        id = this[BlogEntries.id].value,
        createdBy = this[BlogEntries.createdBy],
        timeOfCreation = this[BlogEntries.timeOfCreation],
        modifiedBy = this[BlogEntries.modifiedBy],
        timeOfModification = this[BlogEntries.timeOfModification],
        creatorName = this[BlogEntries.creatorName],
        entry = this[BlogEntries.entry],
        blogId = this[BlogEntries.blogId]
    )
}

data class BlogDao(
    override var id: UUID? = null,
    override var createdBy: String = "todo",
    override var timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var modifiedBy: String = "todo",
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    var created: LocalDate = LocalDate.now(),
    var title: String = "",
    internal var userId: UUID? = null,
) : PersistentDao<BlogDao> {
    private val blogEntryRelations = DaoRelations(
        fetchRelations = JactorPersistenceRepositiesConfig.fetchBlogEntryRelations
    )

    private val userRelation = DaoRelation(
        fetchRelation = JactorPersistenceRepositiesConfig.fetchUserRelation,
    )

    val entries: List<BlogEntryDao>
        get() = blogEntryRelations.fetchRelations(id = id ?: error("Blog is not persisted!"))

    val user: UserDao
        get() = userRelation.fetchRelatedInstance(id = userId) ?: error("Missing user relation for blog!")

    override fun copyWithoutId(): BlogDao = copy(id = null)
    override fun modifiedBy(modifier: String): BlogDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }

    fun toBlog(): Blog = Blog(
        created = created,
        persistent = toPersistent(),
        title = title,
        user = user.toUser()
    )
}

data class BlogEntryDao(
    override var id: UUID? = null,
    override val createdBy: String = "todo",
    override var creatorName: String,
    override var entry: String,
    override var modifiedBy: String = "todo",
    override val timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    internal var blogId: UUID,
) : PersistentDao<BlogEntryDao>, EntryDao {
    private val blogRelation = DaoRelation(
        fetchRelation = JactorPersistenceRepositiesConfig.fetchBlogRelation,
    )

    val blogDao: BlogDao
        get() = blogRelation.fetchRelatedInstance(id = blogId) ?: error("no blog relation?")

    override fun copyWithoutId(): BlogEntryDao = copy(id = null)
    override fun modifiedBy(modifier: String): BlogEntryDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }

    fun toBlogEntry() = BlogEntry(
        persistent = toPersistent(),

        blog = blogDao.toBlog(),
        creatorName = creatorName,
        entry = entry,
    )
}
