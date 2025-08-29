package com.github.jactor.persistence

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
import com.github.jactor.persistence.common.EntryEmbeddable
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.PersistentEntity
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.shared.api.GuestBookDto
import com.github.jactor.shared.api.GuestBookEntryDto
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
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

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
    operator fun get(@PathVariable("id") id: UUID): ResponseEntity<GuestBookDto> {
        return guestBookService.find(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
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
    fun getEntry(@PathVariable("id") id: UUID): ResponseEntity<GuestBookEntryDto> {
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
    fun post(@RequestBody guestBookDto: GuestBookDto): ResponseEntity<GuestBookDto> {
        if (guestBookDto.harIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val guestBookModel = guestBookService.saveOrUpdate(GuestBookModel(guestBookDto = guestBookDto))
        return ResponseEntity(guestBookModel.toDto(), HttpStatus.CREATED)
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
    fun put(@RequestBody guestBookDto: GuestBookDto): ResponseEntity<GuestBookDto> {
        if (guestBookDto.harIkkeIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val guestBookModel = GuestBookModel(guestBookDto = guestBookDto)
        return ResponseEntity(guestBookService.saveOrUpdate(guestBookModel).toDto(), HttpStatus.ACCEPTED)
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
    fun postEntry(@RequestBody guestBookEntryDto: GuestBookEntryDto): ResponseEntity<GuestBookEntryDto> {
        if (guestBookEntryDto.harIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val dto = guestBookService.saveOrUpdate(GuestBookEntryModel(guestBookEntryDto)).toDto()
        return ResponseEntity(dto, HttpStatus.CREATED)
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
    fun putEntry(@RequestBody guestBookEntryDto: GuestBookEntryDto): ResponseEntity<GuestBookEntryDto> {
        if (guestBookEntryDto.harIkkeIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(
            guestBookService.saveOrUpdate(GuestBookEntryModel(guestBookEntryDto = guestBookEntryDto)).toDto(),
            HttpStatus.ACCEPTED,
        )
    }
}

interface GuestBookService {
    fun find(id: UUID): GuestBookModel?
    fun findEntry(id: UUID): GuestBookEntryModel?
    fun saveOrUpdate(guestBookModel: GuestBookModel): GuestBookModel
    fun saveOrUpdate(guestBookEntryModel: GuestBookEntryModel): GuestBookEntryModel
}

@Service
class DefaultGuestBookService(
    private val guestBookRepository: GuestBookRepository,
    private val guestBookEntryRepository: GuestBookEntryRepository
) : GuestBookService {
    override fun find(id: UUID): GuestBookModel? {
        return guestBookRepository.findById(id)
            .map { it.toModel() }
            .orElse(null)
    }

    override fun findEntry(id: UUID): GuestBookEntryModel? {
        return guestBookEntryRepository.findById(id)
            .map { it.toModel() }
            .orElse(null)
    }

    override fun saveOrUpdate(guestBookModel: GuestBookModel): GuestBookModel {
        return guestBookRepository.save(GuestBookEntity(guestBookModel)).toModel()
    }

    override fun saveOrUpdate(guestBookEntryModel: GuestBookEntryModel): GuestBookEntryModel {
        return guestBookEntryRepository.save(GuestBookEntryEntity(guestBookEntryModel)).toModel()
    }
}

@JvmRecord
data class GuestBookModel(
    val persistentModel: PersistentModel = PersistentModel(),
    val entries: Set<GuestBookEntryModel> = emptySet(),
    val title: String? = null,
    val user: UserModel? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(persistentModel: PersistentModel, guestBook: GuestBookModel) : this(
        persistentModel = persistentModel,
        entries = guestBook.entries,
        title = guestBook.title,
        user = guestBook.user
    )

    constructor(guestBookDto: GuestBookDto) : this(
        persistentModel = PersistentModel(guestBookDto.persistentDto),
        entries = guestBookDto.entries.map { GuestBookEntryModel(it) }.toSet(),
        title = guestBookDto.title,
        user = guestBookDto.userDto?.let { UserModel(userDto = it) }
    )

    fun toDto(): GuestBookDto = GuestBookDto(
        persistentDto = persistentModel.toDto(),
        entries = entries.map { entry: GuestBookEntryModel -> entry.toDto() }.toSet(),
        title = title,
        userDto = user?.toDto()
    )
}

@JvmRecord
data class GuestBookEntryModel(
    val creatorName: String? = null,
    val entry: String? = null,
    val guestBook: GuestBookModel? = null,
    val persistentModel: PersistentModel = PersistentModel(),
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("No creator is provided!")
    val notNullableEntry: String
        @JsonIgnore get() = entry ?: throw IllegalStateException("No entry is provided!")

    constructor(
        persistentModel: PersistentModel, guestBookEntry: GuestBookEntryModel
    ) : this(
        persistentModel = persistentModel,
        guestBook = guestBookEntry.guestBook,
        creatorName = guestBookEntry.creatorName,
        entry = guestBookEntry.entry
    )

    constructor(guestBookEntryDto: GuestBookEntryDto) : this(
        persistentModel = PersistentModel(guestBookEntryDto.persistentDto),
        guestBook = guestBookEntryDto.guestBook?.let { GuestBookModel(guestBookDto = it) }
    )

    fun toDto() = GuestBookEntryDto(
        entry = entry,
        creatorName = creatorName,
        guestBook = guestBook?.toDto(),
        persistentDto = persistentModel.toDto(),
    )
}

internal object GuestBookBuilder {
    fun new(guestBookModel: GuestBookModel = GuestBookModel()): GuestBookData = GuestBookData(
        guestBookModel = guestBookModel.copy(
            persistentModel = guestBookModel.persistentModel.copy(id = UUID.randomUUID())
        )
    )

    fun unchanged(guestBookModel: GuestBookModel): GuestBookData = GuestBookData(
        guestBookModel = guestBookModel
    )

    @JvmRecord
    data class GuestBookData(val guestBookModel: GuestBookModel, val guestBookEntryModel: GuestBookEntryModel? = null) {
        fun withEntry(guestBookEntryModel: GuestBookEntryModel): GuestBookData = copy(
            guestBookEntryModel = guestBookEntryModel.copy(
                persistentModel = guestBookModel.persistentModel.copy(id = UUID.randomUUID())
            )
        )

        fun withEntryContainingPersistentId(guestBookEntryModel: GuestBookEntryModel): GuestBookData = copy(
            guestBookEntryModel = guestBookEntryModel
        )

        fun buildGuestBookEntity(): GuestBookEntity = GuestBookEntity(guestBook = guestBookModel)
        fun buildGuestBookEntryEntity(): GuestBookEntryEntity = GuestBookEntryEntity(
            guestBookEntry = guestBookEntryModel ?: error("no guest book entry provided!")
        )
    }
}

interface GuestBookRepository : CrudRepository<GuestBookEntity, UUID> {
    fun findByUser(userEntity: UserEntity): GuestBookEntity?
}

interface GuestBookEntryRepository : CrudRepository<GuestBookEntryEntity, UUID> {
    fun findByGuestBook(guestBookEntity: GuestBookEntity): List<GuestBookEntryEntity>
}

@Entity
@Table(name = "T_GUEST_BOOK")
class GuestBookEntity : PersistentEntity<GuestBookEntity?> {
    @Id
    override var id: UUID? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    lateinit var persistentDataEmbeddable: PersistentDataEmbeddable
        internal set

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
        entries = guestBook.entries.map { it.copyWithoutId() }.toMutableSet()
        id = guestBook.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        title = guestBook.title
        user = guestBook.copyUserWithoutId()
    }

    constructor(guestBook: GuestBookModel) {
        entries = guestBook.entries.map { GuestBookEntryEntity(it) }.toMutableSet()
        id = guestBook.id
        persistentDataEmbeddable = PersistentDataEmbeddable(guestBook.persistentModel)
        title = guestBook.title
        user = guestBook.user?.let { UserEntity(it) }
    }

    private fun copyUserWithoutId(): UserEntity? {
        return user?.copyWithoutId()
    }

    fun toModel(): GuestBookModel {
        return GuestBookModel(
            persistentModel = persistentDataEmbeddable.toModel(id),
            entries = entries.map { it.toModel() }.toMutableSet(),
            title = title,
            user = user?.toModel()
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
}

@Entity
@Table(name = "T_GUEST_BOOK_ENTRY")
class GuestBookEntryEntity : PersistentEntity<GuestBookEntryEntity?> {
    @Id
    override var id: UUID? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    lateinit var persistentDataEmbeddable: PersistentDataEmbeddable
        internal set

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

    constructor(guestBookEntry: GuestBookEntryModel) {
        entryEmbeddable = EntryEmbeddable(guestBookEntry.notNullableCreator, guestBookEntry.notNullableEntry)
        guestBook = guestBookEntry.guestBook?.let { GuestBookEntity(it) }
        id = guestBookEntry.id
        persistentDataEmbeddable = PersistentDataEmbeddable(guestBookEntry.persistentModel)
    }

    private fun copyGuestBookWithoutId(): GuestBookEntity {
        return guestBook!!.copyWithoutId()
    }

    private fun copyEntry(): EntryEmbeddable {
        return entryEmbeddable.copy()
    }

    fun toModel(): GuestBookEntryModel {
        return GuestBookEntryModel(
            creatorName = entryEmbeddable.creatorName,
            entry = entryEmbeddable.entry,
            guestBook = guestBook?.toModel(),
            persistentModel = persistentDataEmbeddable.toModel(id),
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
}
