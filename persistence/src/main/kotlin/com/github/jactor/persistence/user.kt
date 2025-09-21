package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
import com.github.jactor.shared.whenTrue
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping(path = ["/user"], produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(private val userService: UserService) {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User found"),
            ApiResponse(responseCode = "204", description = "No user with username")
        ]
    )
    @GetMapping("/name/{username}")
    @Operation(description = "Find a user by its username")
    suspend fun find(@PathVariable("username") username: String): ResponseEntity<UserDto> {
        return userService.find(username = username)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User got"),
            ApiResponse(responseCode = "404", description = "Did not find user with id")
        ]
    )
    @GetMapping("/{id}")
    @Operation(description = "Get a user by its id")
    suspend operator fun get(@PathVariable("id") id: UUID): ResponseEntity<UserDto> {
        return userService.find(id)?.let { ResponseEntity(it.toDto(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User created"),
            ApiResponse(responseCode = "400", description = "Username already occupied or no body is present")
        ]
    )
    @Operation(description = "Create a user")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun post(
        @RequestBody createUserCommand: CreateUserCommand
    ): ResponseEntity<UserDto> = when (userService.isAlreadyPersisted(username = createUserCommand.username)) {
        true -> ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST)
        false -> ResponseEntity(userService.create(createUserCommand).toDto(), HttpStatus.CREATED)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "User updated"),
            ApiResponse(responseCode = "400", description = "Did not find user with id or no body is present")
        ]
    )
    @Operation(description = "Update a user by its id")
    @PutMapping("/update")
    suspend fun put(@RequestBody userDto: UserDto): ResponseEntity<UserDto> = when (userDto.harIkkeIdentifikator()) {
        true -> ResponseEntity(HttpStatus.BAD_REQUEST)
        false -> userService.update(user = User(userDto = userDto)).let {
            ResponseEntity(it.toDto(), HttpStatus.ACCEPTED)
        }
    }

    @ApiResponses(ApiResponse(responseCode = "200", description = "List of usernames found"))
    @GetMapping("/usernames")
    @Operation(description = "Find all usernames for a user type")
    suspend fun findAllUsernames(
        @RequestParam(required = false, defaultValue = "ACTIVE") userType: String
    ): ResponseEntity<List<String>> = userService.findUsernames(userType = UserDao.UserType.valueOf(userType)).let {
        when (it.isEmpty()) {
            true -> ResponseEntity(HttpStatus.NO_CONTENT)
            false -> ResponseEntity(it, HttpStatus.OK)
        }
    }
}

@Service
class UserService {
    suspend fun find(username: String): User? = UserRepository.findByUsername(username)?.toUser()
    suspend fun find(id: UUID): User? = UserRepository.findUserById(userId = id)?.toUser()

    @Transactional
    suspend fun update(user: User): User = UserRepository.update(user)
    suspend fun create(createUserCommand: CreateUserCommand): User = createUserCommand.toUserDto().toUserDao()
        .let { UserRepository.insert(user = it).toUser() }

    suspend fun findUsernames(userType: UserDao.UserType): List<String> = listOf(userType).let { type ->
        UserRepository.findByUserTypeIn(userType = type).map { it.username }
    }

    suspend fun isAlreadyPersisted(username: String): Boolean = UserRepository.contains(username)
}

@JvmRecord
data class User(
    val persistent: Persistent = Persistent(),
    val person: Person?,
    val emailAddress: String?,
    val username: String?,
    val usertype: Usertype,
) {
    val id: UUID?
        get() = persistent.id

    constructor(persistent: Persistent, user: User) : this(
        persistent = persistent,
        emailAddress = user.emailAddress,
        person = user.person,
        username = user.username,
        usertype = user.usertype
    )

    constructor(
        persistent: Persistent,
        personInternal: Person?,
        emailAddress: String?,
        username: String?
    ) : this(
        persistent = persistent,
        person = personInternal,
        emailAddress = emailAddress,
        username = username,
        usertype = Usertype.ACTIVE
    )

    constructor(userDto: UserDto) : this(
        persistent = userDto.persistentDto.toPersistent(),
        person = userDto.person?.toPerson(),
        emailAddress = userDto.emailAddress,
        username = userDto.username,
        usertype = Usertype.valueOf(userDto.userType.name)
    )

    fun toDto() = UserDto(
        persistentDto = persistent.toPersistentDto(),
        emailAddress = emailAddress,
        person = person?.toPersonDto(),
        username = username,
        userType = (usertype == Usertype.ADMIN).whenTrue { UserType.ACTIVE } ?: UserType.valueOf(usertype.name)
    )

    fun toUserDao() = UserDao(user = this)

    enum class Usertype {
        ADMIN, ACTIVE, INACTIVE
    }
}

object Users : UUIDTable(name = "T_USER", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val emailAddress = text("EMAIL").nullable()
    val username = text("USERNAME")
    val personId = uuid("PERSON_ID").references(People.id)
    val userType = text("USER_TYPE")
    val inactiveSince = datetime("INACTIVE_SINCE").nullable()
}

object UserRepository {
    fun contains(username: String): Boolean = transaction {
        Users
            .select(Users.username)
            .where { Users.username eq username }
            .count() > 0
    }

    fun findByUserTypeIn(userType: List<UserDao.UserType>): List<UserDao> = transaction { emptyList() }
    fun findByUsername(username: String): UserDao? = transaction {
        Users
            .selectAll()
            .where { Users.username eq username }
            .singleOrNull()
            ?.toUserDao()

    }

    fun findUserById(userId: UUID): UserDao? = transaction {
        Users
            .selectAll()
            .where { Users.id eq userId }
            .singleOrNull()
            ?.toUserDao()
    }

    fun insert(user: UserDao): UserDao = transaction {
        Users.insertAndGetId { row ->
            row[createdBy] = user.createdBy
            row[modifiedBy] = user.modifiedBy
            row[timeOfCreation] = user.timeOfCreation
            row[timeOfModification] = user.timeOfModification
            row[emailAddress] = user.emailAddress
            row[username] = user.username
            row[personId] = requireNotNull(user.personId) { "Person id cannot be null when inserting a user." }
            row[userType] = user.userType.name
        }.let { newId -> user.also { it.id = newId.value } }
    }

    fun update(user: User): User = transaction {
        requireNotNull(user.id) { "User must have an id!" }

        Users.update({ Users.id eq user.id }) {
            it[modifiedBy] = user.persistent.modifiedBy
            it[timeOfModification] = user.persistent.timeOfModification
            it[emailAddress] = user.emailAddress
            it[username] = requireNotNull(user.username) { "Username cannot be null when updating a user." }
            it[userType] = user.usertype.name
            it[personId] =
                requireNotNull(user.person?.persistent?.id) { "Person id cannot be null when updating a user." }
            // createdBy & timeOfCreation are intentionally not updated
        }

        user
    }

    fun ResultRow.toUserDao(id: UUID? = null): UserDao = UserDao(
        id = id ?: this[Users.id].value,
        createdBy = this[Users.createdBy],
        timeOfCreation = this[Users.timeOfCreation],
        modifiedBy = this[Users.modifiedBy],
        timeOfModification = this[Users.timeOfModification],
        username = this[Users.username],
        emailAddress = this[Users.emailAddress],
        personId = this[Users.personId],
    )
}

data class UserDao(
    override var id: UUID? = null,
    override val createdBy: String,
    override val timeOfCreation: LocalDateTime,
    override var modifiedBy: String,
    override var timeOfModification: LocalDateTime,

    internal var userType: UserType = UserType.ACTIVE,
    internal var emailAddress: String? = null,
    internal var personId: UUID? = null,
    internal var username: String = "na",
) : PersistentDao<UserDao> {
    val personDao: PersonDao?
        get() = personId?.let { PersonRepository.findById(id = it) }

    constructor(user: User) : this(
        id = user.persistent.id,
        createdBy = user.persistent.createdBy,
        modifiedBy = user.persistent.modifiedBy,
        timeOfCreation = user.persistent.timeOfCreation,
        timeOfModification = user.persistent.timeOfModification,

        emailAddress = user.emailAddress,
        personId = user.person?.persistent?.id,
        username = user.username ?: "na",
        userType = UserType.entries
            .firstOrNull { aUserType: UserType -> aUserType.name == user.usertype.name }
            ?: error(message = "Unknown UserType: ${user.usertype}"),
    )

    override fun copyWithoutId(): UserDao = copy(
        id = null,
        personId = null,
    )

    override fun modifiedBy(modifier: String): UserDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }

    fun toUser() = User(
        persistent = toPersistent(),
        username = username,
        emailAddress = emailAddress,
        person = personDao?.toPerson(),
        usertype = User.Usertype.entries.firstOrNull { it.name == userType.name }
            ?: error("Unknown UserType: $userType")
    )

    enum class UserType {
        ADMIN, ACTIVE, INACTIVE
    }
}
