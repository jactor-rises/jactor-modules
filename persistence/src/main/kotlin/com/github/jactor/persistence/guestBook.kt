package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertIgnoreAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
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
import com.github.jactor.persistence.common.DaoRelation
import com.github.jactor.persistence.common.EntryDao
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.persistence.util.toCreateGuestBook
import com.github.jactor.persistence.util.toGuestBook
import com.github.jactor.persistence.util.toPersistent
import com.github.jactor.persistence.util.toUser
import com.github.jactor.shared.api.CreateGuestBookCommand
import com.github.jactor.shared.api.CreateGuestBookEntryCommand
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping(value = ["/guestBook"], produces = [MediaType.APPLICATION_JSON_VALUE])
class GuestBookController(private val guestBookService: GuestBookService) {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Gjesteboka er hentet"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ingen gjestebok på id",
            )
        ]
    )
    @GetMapping("/{id}")
    @Operation(description = "Henter en gjesdebok ved å angi id")
    suspend operator fun get(@PathVariable("id") id: UUID): ResponseEntity<GuestBookDto> {
        return guestBookService.findGuestBook(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Innslaget i gjesteboka er hentet"),
            ApiResponse(
                responseCode = "204",
                description = "Fant ingen innslag med id",
            )
        ]
    )
    @GetMapping("/entry/{id}")
    @Operation(description = "Hent et innslag i en gjesdebok ved å angi id til innslaget")
    suspend fun getEntry(@PathVariable("id") id: UUID): ResponseEntity<GuestBookEntryDto> {
        return guestBookService.findEntry(id)?.let { ResponseEntity(it.toGuestBookEntryDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Gjesteboka er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen gjestebok er gitt eller gjesteboka er allerede opprettet",
            )
        ]
    )
    @Operation(description = "Opprett en gjestebok")
    @PostMapping
    suspend fun post(
        @RequestBody createGuestBookCommand: CreateGuestBookCommand
    ): ResponseEntity<GuestBookDto> = ResponseEntity(
        guestBookService.create(createGuestBook = createGuestBookCommand.toCreateGuestBook()).toGuestBookDto(),
        HttpStatus.CREATED
    )

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Gjesteboka er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen gjestebok er gitt eller det mangler gjestebok å endre for id",
            )
        ]
    )
    @Operation(description = "Endre en gjestebok")
    @PutMapping("/update")
    suspend fun put(
        @RequestBody guestBookDto: GuestBookDto
    ): ResponseEntity<GuestBookDto> = when (guestBookDto.harIkkeIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            guestBookService.saveOrUpdate(GuestBook(guestBookDto = guestBookDto)).toDto(),
            HttpStatus.ACCEPTED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Innslaget i gjesteboka er opprettet"),
            ApiResponse(responseCode = "400", description = "Ingen innslag som skal opprettes er gitt"),
            ApiResponse(responseCode = "400", description = "Ingen forfatter av innslag som skal opprettes er gitt"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen id til gjesteboka for innslaget som skal opprettes er gitt",
            ),
        ]
    )
    @Operation(description = "Opprett et innslag i en gjestebok")
    @PostMapping("/entry")
    suspend fun postEntry(
        @RequestBody createGuestBookEntryCommand: CreateGuestBookEntryCommand
    ): ResponseEntity<GuestBookEntryDto> = ResponseEntity(
        guestBookService.create(
            createGuestBookEntry = createGuestBookEntryCommand.toCreateGuestBook()
        ).toGuestBookEntryDto(),
        HttpStatus.CREATED
    )

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Innslaget i gjesteboka er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen id til innslag for gjestebok er gitt",
            )
        ]
    )
    @Operation(description = "Endre et innslag i en gjestebok")
    @PutMapping("/entry/update")
    suspend fun putEntry(
        @RequestBody guestBookEntryDto: GuestBookEntryDto
    ): ResponseEntity<GuestBookEntryDto> = when (guestBookEntryDto.harIkkeIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            guestBookService.saveOrUpdate(GuestBookEntry(guestBookEntryDto = guestBookEntryDto)).toGuestBookEntryDto(),
            HttpStatus.ACCEPTED,
        )
    }
}

interface GuestBookService {
    suspend fun create(createGuestBook: CreateGuestBook): GuestBook
    suspend fun create(createGuestBookEntry: CreateGuestBookEntry): GuestBookEntry
    suspend fun findGuestBook(id: UUID): GuestBook?
    suspend fun findEntry(id: UUID): GuestBookEntry?
    suspend fun saveOrUpdate(guestBook: GuestBook): GuestBook
    suspend fun saveOrUpdate(guestBookEntry: GuestBookEntry): GuestBookEntry
}

@Service
class GuestBookServiceBean(private val guestBookRepository: GuestBookRepository) : GuestBookService {
    override suspend fun create(createGuestBook: CreateGuestBook): GuestBook {
        return guestBookRepository.save(
            guestBookDao = GuestBookDao().apply {
                title = createGuestBook.title
                userId = createGuestBook.userId
            }
        ).toGuestBook()
    }

    override suspend fun create(createGuestBookEntry: CreateGuestBookEntry): GuestBookEntry {
        return guestBookRepository.save(
            guestBookEntryDao = GuestBookEntryDao(
                creatorName = createGuestBookEntry.creatorName,
                entry = createGuestBookEntry.entry,
                guestBookId = createGuestBookEntry.guestBookId
            )
        ).toGuestBookEntry()
    }

    override suspend fun findGuestBook(id: UUID): GuestBook? {
        return guestBookRepository.findGuestBookById(id)?.toGuestBook()
    }

    override suspend fun findEntry(id: UUID): GuestBookEntry? {
        return guestBookRepository.findGuestBookEntryById(id)?.toGuestBookEntry()
    }

    override suspend fun saveOrUpdate(guestBook: GuestBook): GuestBook {
        return guestBookRepository.save(guestBookDao = guestBook.toGuestBookDao()).toGuestBook()
    }

    override suspend fun saveOrUpdate(guestBookEntry: GuestBookEntry): GuestBookEntry {
        return guestBookRepository.save(guestBookEntryDao = guestBookEntry.toGuestBookEntryDao()).toGuestBookEntry()
    }
}

@JvmRecord
data class GuestBook(
    val persistent: Persistent,
    val entries: Set<GuestBookEntry>,
    val title: String?,
    val user: User?,
) {
    val id: UUID? get() = persistent.id

    constructor(persistent: Persistent, guestBook: GuestBook) : this(
        persistent = persistent,
        entries = guestBook.entries,
        title = guestBook.title,
        user = guestBook.user
    )

    constructor(guestBookDto: GuestBookDto) : this(
        persistent = guestBookDto.persistentDto.toPersistent(),
        entries = guestBookDto.entries.map { GuestBookEntry(it) }.toSet(),
        title = guestBookDto.title,
        user = guestBookDto.userDto?.toUser()
    )

    fun toDto(): GuestBookDto = GuestBookDto(
        persistentDto = persistent.toPersistentDto(),
        entries = entries.map { entry: GuestBookEntry -> entry.toGuestBookEntryDto() }.toSet(),
        title = title,
        userDto = user?.toUserDto()
    )

    fun toGuestBookDao() = GuestBookDao(
        id = id,
        createdBy = persistent.createdBy,
        modifiedBy = persistent.modifiedBy,
        timeOfCreation = persistent.timeOfCreation,
        timeOfModification = persistent.timeOfModification,
        title = requireNotNull(title) { "Title cannot be null!" },
        userId = user?.id,
    )

    fun toGuestBookDto() = GuestBookDto(
        persistentDto = persistent.toPersistentDto(),
        entries = entries.map { it.toGuestBookEntryDto() }.toSet(),
        title = title,
        userDto = user?.toUserDto()
    )
}

@JvmRecord
data class CreateGuestBook(
    val userId: UUID,
    val title: String,
)

@JvmRecord
data class CreateGuestBookEntry(
    val guestBookId: UUID,
    val creatorName: String,
    val entry: String,
)

@JvmRecord
data class GuestBookEntry(
    val creatorName: String,
    val entry: String,
    val guestBook: GuestBook?,
    val persistent: Persistent,
) {
    val id: UUID? get() = persistent.id

    constructor(guestBookEntryDto: GuestBookEntryDto) : this(
        creatorName = requireNotNull(guestBookEntryDto.creatorName) { "Creator name cannot be null!" },
        entry = requireNotNull(guestBookEntryDto.entry) { "Entry cannot be null!" },
        persistent = guestBookEntryDto.persistentDto.toPersistent(),
        guestBook = guestBookEntryDto.guestBook?.toGuestBook(),
    )

    fun toGuestBookEntryDto() = GuestBookEntryDto(
        entry = entry,
        creatorName = creatorName,
        guestBook = guestBook?.toDto(),
        persistentDto = persistent.toPersistentDto(),
    )

    fun toGuestBookEntryDao() = GuestBookEntryDao(
        id = id,
        createdBy = persistent.createdBy,
        modifiedBy = persistent.modifiedBy,
        timeOfCreation = persistent.timeOfCreation,
        timeOfModification = persistent.timeOfModification,
        creatorName = creatorName,
        entry = entry,
        guestBookId = guestBook?.persistent?.id ?: error("Guest book must have an id!"),
    )
}

object GuestBooks : UUIDTable(name = "T_GUEST_BOOK", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")
    val title = text("TITLE")
    val userId = uuid("USER_ID").references(Users.id)
}

object GuestBookEntries : UUIDTable(name = "T_GUEST_BOOK_ENTRY", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val creatorName = text("CREATOR_NAME")
    val entry = text("ENTRY")
    val guestBookId = uuid("GUEST_BOOK_ID").references(GuestBooks.id)
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")
}

interface GuestBookRepository {
    fun findAllGuestBooks(): List<GuestBookDao>
    fun findByUserId(userId: UUID): GuestBookDao?
    fun findGuestBookById(id: UUID): GuestBookDao?
    fun findGuestBookEntryById(id: UUID): GuestBookEntryDao?
    fun findGuestBookByUser(user: UserDao): GuestBookDao?
    fun findGuestBookEntryByGuestBook(guestBookDao: GuestBookDao): List<GuestBookEntryDao>
    fun save(guestBookDao: GuestBookDao): GuestBookDao
    fun save(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao
}

object GuestBookRepositoryObject : GuestBookRepository {
    override fun findAllGuestBooks(): List<GuestBookDao> = transaction {
        GuestBooks.selectAll().map { it.toGuestBookDao() }
    }

    override fun findByUserId(userId: UUID): GuestBookDao? = transaction {
        GuestBooks.selectAll()
            .andWhere { GuestBooks.userId eq userId }
            .map { it.toGuestBookDao() }
            .singleOrNull()
    }

    override fun findGuestBookById(id: UUID): GuestBookDao? = transaction {
        GuestBooks.selectAll()
            .andWhere { GuestBooks.id eq id }
            .singleOrNull()?.toGuestBookDao()
    }

    override fun findGuestBookEntryById(id: UUID): GuestBookEntryDao? = transaction {
        GuestBookEntries.selectAll()
            .andWhere { GuestBookEntries.id eq id }
            .singleOrNull()?.toGuestBookEntryDao()
    }

    override fun findGuestBookByUser(user: UserDao): GuestBookDao? = transaction {
        require(user.isPersisted) { "A search of a guestbook requires a persistent user!" }
        GuestBooks.selectAll()
            .andWhere { GuestBooks.userId eq user.id!! }
            .singleOrNull()?.toGuestBookDao()
    }

    override fun findGuestBookEntryByGuestBook(guestBookDao: GuestBookDao): List<GuestBookEntryDao> = transaction {
        require(guestBookDao.isPersisted) { "A search of entries requires a persistent guestbook!" }
        GuestBookEntries.selectAll()
            .andWhere { GuestBookEntries.guestBookId eq guestBookDao.id!! }
            .map { it.toGuestBookEntryDao() }
    }

    override fun save(guestBookDao: GuestBookDao): GuestBookDao = transaction {
        when (guestBookDao.isPersisted) {
            true -> update(guestBookDao)
            false -> insert(guestBookDao)
        }
    }

    private fun update(guestBookDao: GuestBookDao): GuestBookDao = GuestBooks.update(
        where = { GuestBooks.id eq guestBookDao.id }
    ) {
        it[createdBy] = guestBookDao.createdBy
        it[title] = guestBookDao.title
        it[modifiedBy] = guestBookDao.modifiedBy
        it[timeOfCreation] = guestBookDao.timeOfCreation
        it[timeOfModification] = guestBookDao.timeOfModification
        it[userId] = requireNotNull(guestBookDao.userId) { "UserId cannot be null!" }
    }.let { guestBookDao }

    private fun insert(guestBookDao: GuestBookDao): GuestBookDao = GuestBooks.insertIgnoreAndGetId {
        it[createdBy] = guestBookDao.createdBy
        it[title] = guestBookDao.title
        it[modifiedBy] = guestBookDao.modifiedBy
        it[timeOfCreation] = guestBookDao.timeOfCreation
        it[timeOfModification] = guestBookDao.timeOfModification
        it[userId] = requireNotNull(guestBookDao.userId) { "UserId cannot be null!" }
    }?.value.let { guestBookDao.copy(id = it) }

    override fun save(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao = transaction {
        when (guestBookEntryDao.isPersisted) {
            true -> update(guestBookEntryDao)
            false -> insert(guestBookEntryDao)
        }
    }

    private fun update(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao = GuestBookEntries.update(
        where = { GuestBookEntries.id eq guestBookEntryDao.id }
    ) {
        it[createdBy] = guestBookEntryDao.createdBy
        it[creatorName] = guestBookEntryDao.creatorName
        it[entry] = guestBookEntryDao.entry
        it[modifiedBy] = guestBookEntryDao.modifiedBy
        it[timeOfCreation] = guestBookEntryDao.timeOfCreation
        it[timeOfModification] = guestBookEntryDao.timeOfModification
    }.let { guestBookEntryDao }

    private fun insert(
        guestBookEntryDao: GuestBookEntryDao
    ): GuestBookEntryDao = GuestBookEntries.insertIgnoreAndGetId {
        it[createdBy] = guestBookEntryDao.createdBy
        it[creatorName] = guestBookEntryDao.creatorName
        it[entry] = guestBookEntryDao.entry
        it[modifiedBy] = guestBookEntryDao.modifiedBy
        it[timeOfCreation] = guestBookEntryDao.timeOfCreation
        it[timeOfModification] = guestBookEntryDao.timeOfModification
    }?.value.let { guestBookEntryDao.copy(id = it) }
}

fun ResultRow.toGuestBookDao() = GuestBookDao(
    id = this[GuestBooks.id].value,
    createdBy = this[GuestBooks.createdBy],
    timeOfCreation = this[GuestBooks.timeOfCreation],
    modifiedBy = this[GuestBooks.modifiedBy],
    timeOfModification = this[GuestBooks.timeOfModification],

    title = this[GuestBooks.title],
    userId = this[GuestBooks.userId],
)

fun ResultRow.toGuestBookEntryDao() = GuestBookEntryDao(
    id = this[GuestBookEntries.id].value,
    createdBy = this[GuestBookEntries.createdBy],
    timeOfCreation = this[GuestBookEntries.timeOfCreation],
    modifiedBy = this[GuestBookEntries.modifiedBy],
    timeOfModification = this[GuestBookEntries.timeOfModification],

    creatorName = this[GuestBookEntries.creatorName],
    entry = this[GuestBookEntries.entry],
    guestBookId = this[GuestBookEntries.guestBookId],
)

data class GuestBookDao(
    override var id: UUID? = null,
    override var createdBy: String = "todo",
    override var timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var modifiedBy: String = "todo",
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    var title: String = "no-name",
    var entries: MutableSet<GuestBookEntryDao> = mutableSetOf(),
    internal var userId: UUID? = null,
) : PersistentDao<GuestBookDao?> {
    private val userRelation = DaoRelation(
        fetchRelation = JactorPersistenceRepositiesConfig.fetchUserRelation,
    )

    val user: UserDao
        get() = userRelation.fetchRelatedInstance(id = userId) ?: error("no user relation?")

    fun toGuestBook(): GuestBook = GuestBook(
        persistent = toPersistent(),
        entries = entries.map { it.toGuestBookEntry() }.toMutableSet(),
        title = title,
        user = user.toUser()
    )

    override fun copyWithoutId(): GuestBookDao = copy(
        id = null,
    )

    override fun modifiedBy(modifier: String): GuestBookDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }
}

data class GuestBookEntryDao(
    override var id: UUID? = null,
    override val createdBy: String = "todo",
    override var modifiedBy: String = "todo",
    override val timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    override var creatorName: String,
    override var entry: String,
    var guestBookId: UUID? = null
) : PersistentDao<GuestBookEntryDao>, EntryDao {
    val guestBookRelation = DaoRelation(
        fetchRelation = JactorPersistenceRepositiesConfig.fetchGuestBookRelation,
    )

    val guestBookDao: GuestBookDao
        get() = guestBookRelation.fetchRelatedInstance(id = guestBookId)
            ?: error("No guest book relation for entry with id $guestBookId exists")

    fun toGuestBookEntry() = GuestBookEntry(
        persistent = toPersistent(),
        creatorName = creatorName,
        entry = entry,
        guestBook = guestBookDao.toGuestBook(),
    )

    override fun copyWithoutId() = copy(
        id = null,
        guestBookId = null,
    )

    override fun modifiedBy(modifier: String): GuestBookEntryDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }
}
