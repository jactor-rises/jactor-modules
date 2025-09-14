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
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
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
                content = arrayOf(Content(schema = Schema(hidden = true)))
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
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @GetMapping("/entry/{id}")
    @Operation(description = "Hent et innslag i en gjesdebok ved å angi id til innslaget")
    suspend fun getEntry(@PathVariable("id") id: UUID): ResponseEntity<GuestBookEntryDto> {
        return guestBookService.findEntry(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Gjesteboka er opprettet"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen gjestebok er gitt eller gjesteboka er allerede opprettet",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @Operation(description = "Opprett en gjestebok")
    @PostMapping
    suspend fun post(
        @RequestBody guestBookDto: GuestBookDto
    ): ResponseEntity<GuestBookDto> = when (guestBookDto.harIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            guestBookService.saveOrUpdate(GuestBook(guestBookDto = guestBookDto)).toDto(),
            HttpStatus.CREATED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Gjesteboka er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen gjestebok er gitt eller det mangler gjestebok å endre for id",
                content = arrayOf(Content(schema = Schema(hidden = true)))
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
            ApiResponse(
                responseCode = "400",
                description = "Ingen id til innslag å opprette",
                content = arrayOf(Content(schema = Schema(hidden = true)))
            )
        ]
    )
    @Operation(description = "Opprett et innslag i en gjestebok")
    @PostMapping("/entry")
    suspend fun postEntry(
        @RequestBody guestBookEntryDto: GuestBookEntryDto
    ): ResponseEntity<GuestBookEntryDto> = when (guestBookEntryDto.harIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(
            guestBookService.saveOrUpdate(GuestBookEntry(guestBookEntryDto)).toDto(),
            HttpStatus.CREATED
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Innslaget i gjesteboka er endret"),
            ApiResponse(
                responseCode = "400",
                description = "Ingen id til innslag for gjestebok er gitt",
                content = arrayOf(Content(schema = Schema(hidden = true)))
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
            guestBookService.saveOrUpdate(GuestBookEntry(guestBookEntryDto = guestBookEntryDto)).toDto(),
            HttpStatus.ACCEPTED,
        )
    }
}

interface GuestBookService {
    suspend fun findGuestBook(id: UUID): GuestBook?
    suspend fun findEntry(id: UUID): GuestBookEntry?
    suspend fun saveOrUpdate(guestBook: GuestBook): GuestBook
    suspend fun saveOrUpdate(guestBookEntry: GuestBookEntry): GuestBookEntry
}

@Service
class DefaultGuestBookService : GuestBookService {
    override suspend fun findGuestBook(id: UUID): GuestBook? {
        return GuestBookRepository.findGuestBookById(id)?.toGuestBook()
    }

    override suspend fun findEntry(id: UUID): GuestBookEntry? {
        return GuestBookRepository.findGuestBookEntryById(id)?.toGuestBookEntry()
    }

    override suspend fun saveOrUpdate(guestBook: GuestBook): GuestBook {
        return GuestBookRepository.insertOrUpdate(GuestBookDao(guestBook)).toGuestBook()
    }

    override suspend fun saveOrUpdate(guestBookEntry: GuestBookEntry): GuestBookEntry {
        return GuestBookRepository.insertOrUpdate(GuestBookEntryDao(guestBookEntry)).toGuestBookEntry()
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
        user = guestBookDto.userDto?.let { User(userDto = it) }
    )

    fun toDto(): GuestBookDto = GuestBookDto(
        persistentDto = persistent.toPersistentDto(),
        entries = entries.map { entry: GuestBookEntry -> entry.toDto() }.toSet(),
        title = title,
        userDto = user?.toDto()
    )

    fun withId(): GuestBook = copy(persistent = persistent.copy(id = id ?: UUID.randomUUID()))
    fun toEntity() = GuestBookDao(guestBook = this)
}

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

    fun toDto() = GuestBookEntryDto(
        entry = entry,
        creatorName = creatorName,
        guestBook = guestBook?.toDto(),
        persistentDto = persistent.toPersistentDto(),
    )

    fun toEntity() = GuestBookEntryDao(guestBookEntry = this)
    fun withId(): GuestBookEntry = copy(persistent = persistent.copy(id = id ?: UUID.randomUUID()))
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

object GuestBookRepository {
    fun findGuestBookById(id: UUID): GuestBookDao? = transaction {
        GuestBooks.selectAll()
            .andWhere { GuestBooks.id eq id }
            .singleOrNull()?.toGuestBookDao()
    }

    fun findGuestBookEntryById(id: UUID): GuestBookEntryDao? = transaction {
        GuestBookEntries.selectAll()
            .andWhere { GuestBookEntries.id eq id }
            .singleOrNull()?.toGuestBookEntryDao()
    }

    fun findGuestBookByUser(user: UserDao): GuestBookDao? = null
    fun findGuestBookEntryByGuestBook(guestBookDao: GuestBookDao): List<GuestBookEntryDao> = emptyList()
    fun insertOrUpdate(guestBookDao: GuestBookDao): GuestBookDao = transaction {
        guestBookDao.id?.let { id ->
            GuestBooks.update({ GuestBooks.id eq id }) {
                it[createdBy] = guestBookDao.createdBy
                it[title] = requireNotNull(guestBookDao.title) { "Title cannot be null!" }
                it[modifiedBy] = guestBookDao.modifiedBy
                it[timeOfCreation] = guestBookDao.timeOfCreation
                it[timeOfModification] = guestBookDao.timeOfModification
                it[userId] = requireNotNull(guestBookDao.userId) { "UserId cannot be null!" }
            }

            guestBookDao
        } ?: run {
            val id = GuestBooks.insertIgnoreAndGetId {
                it[createdBy] = guestBookDao.createdBy
                it[title] = requireNotNull(guestBookDao.title) { "Title cannot be null!" }
                it[modifiedBy] = guestBookDao.modifiedBy
                it[timeOfCreation] = guestBookDao.timeOfCreation
                it[timeOfModification] = guestBookDao.timeOfModification
                it[userId] = requireNotNull(guestBookDao.userId) { "UserId cannot be null!" }
            }?.value

            guestBookDao.copy(id = id)
        }
    }

    fun insertOrUpdate(guestBookEntryDao: GuestBookEntryDao): GuestBookEntryDao = transaction {
        guestBookEntryDao.id?.let { id ->
            GuestBookEntries.update({ GuestBookEntries.id eq id }) {
                it[createdBy] = guestBookEntryDao.createdBy
                it[creatorName] = guestBookEntryDao.creatorName
                it[entry] = guestBookEntryDao.entry
                it[modifiedBy] = guestBookEntryDao.modifiedBy
                it[timeOfCreation] = guestBookEntryDao.timeOfCreation
                it[timeOfModification] = guestBookEntryDao.timeOfModification
            }

            guestBookEntryDao
        } ?: run {
            val id = GuestBookEntries.insertIgnoreAndGetId {
                it[createdBy] = guestBookEntryDao.createdBy
                it[creatorName] = guestBookEntryDao.creatorName
                it[entry] = guestBookEntryDao.entry
                it[modifiedBy] = guestBookEntryDao.modifiedBy
                it[timeOfCreation] = guestBookEntryDao.timeOfCreation
                it[timeOfModification] = guestBookEntryDao.timeOfModification
            }?.value

            guestBookEntryDao.copy(id = id)
        }
    }
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
    var entries: MutableSet<GuestBookEntryDao> = HashSet(),
    internal var userId: UUID? = null,
) : PersistentDao<GuestBookDao?> {
    val user: UserDao by lazy { userId?.let { UserRepository.findUserById(userId = it) } ?: error("no user relation?") }

    constructor(guestBook: GuestBook) : this(
        id = guestBook.id,
        createdBy = guestBook.persistent.createdBy,
        modifiedBy = guestBook.persistent.modifiedBy,
        timeOfCreation = guestBook.persistent.timeOfCreation,
        timeOfModification = guestBook.persistent.timeOfModification,
        entries = guestBook.entries.map { GuestBookEntryDao(it) }.toMutableSet(),
        title = requireNotNull(guestBook.title) { "Title cannot be null!" },
        userId = guestBook.persistent.id,
    )

    fun toGuestBook(): GuestBook = GuestBook(
        persistent = toPersistent(),
        entries = entries.map { it.toGuestBookEntry() }.toMutableSet(),
        title = title,
        user = user.toUser()
    )

    override fun copyWithoutId(): GuestBookDao = copy(
        id = null,
        entries = entries.map { it.copyWithoutId() }.toMutableSet(),
    )

    override fun modifiedBy(modifier: String): GuestBookDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }

    fun add(guestBookEntry: GuestBookEntryDao) {
        entries.add(guestBookEntry)
        guestBookEntry.guestBookId = id ?: error("guest book must have an id before adding entries")
    }
}

data class GuestBookEntryDao(
    override var id: UUID? = null,
    override val createdBy: String,
    override var modifiedBy: String,
    override val timeOfCreation: LocalDateTime,
    override var timeOfModification: LocalDateTime,

    override var creatorName: String,
    override var entry: String,
    var guestBookId: UUID? = null
) : PersistentDao<GuestBookEntryDao>, EntryDao {
    val guestBookDao: GuestBookDao by lazy {
        guestBookId?.let { GuestBookRepository.findGuestBookById(id = it) } ?: error("no guest book relation?")
    }

    constructor(guestBookEntry: GuestBookEntry) : this(
        id = guestBookEntry.id,
        createdBy = guestBookEntry.persistent.createdBy,
        modifiedBy = guestBookEntry.persistent.modifiedBy,
        timeOfCreation = guestBookEntry.persistent.timeOfCreation,
        timeOfModification = guestBookEntry.persistent.timeOfModification,

        creatorName = guestBookEntry.creatorName,
        entry = guestBookEntry.entry,
        guestBookId = guestBookEntry.guestBook?.persistent?.id,
    )

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
