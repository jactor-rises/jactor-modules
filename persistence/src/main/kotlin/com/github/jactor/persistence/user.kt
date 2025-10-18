package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
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
import com.github.jactor.persistence.common.DaoRelation
import com.github.jactor.persistence.common.DaoRelations
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.persistence.util.toCreateUser
import com.github.jactor.persistence.util.toUser
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
        return userService.find(username = username)?.let { ResponseEntity(it.toUserDto(), HttpStatus.OK) }
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
        return userService.find(id)?.let { ResponseEntity(it.toUserDto(), HttpStatus.OK) }
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
        false -> ResponseEntity(
            userService.create(createUserCommand.toCreateUser()).toUserDto(),
            HttpStatus.CREATED
        )
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
        false -> ResponseEntity(
            userService.update(user = userDto.toUser()).toUserDto(),
            HttpStatus.ACCEPTED
        )
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
class UserService(private val userRepository: UserRepository = UserRepositoryObject) {
    suspend fun find(username: String): User? = userRepository.findByUsername(username)?.toUser()
    suspend fun find(id: UUID): User? = userRepository.findById(id = id)?.toUser()

    @Transactional
    suspend fun update(user: User): User = userRepository.save(userDao = user.toUserDao()).toUser()
    suspend fun create(createUser: CreateUser): User = userRepository.save(userDao = createUser.toUserDao()).toUser()
    suspend fun findUsernames(userType: UserDao.UserType): List<String> = userRepository.findUsernames(
        userType = listOf(userType)
    )

    suspend fun isAlreadyPersisted(username: String): Boolean = userRepository.contains(username)
}

@JvmRecord
data class User(
    val persistent: Persistent = Persistent(),
    val person: Person?,
    val emailAddress: String?,
    val username: String?,
    val usertype: Usertype,
) {
    val id: UUID
        get() = persistent.id ?: error("User is not persisted!")

    fun toUserDao() = UserDao(
        id = persistent.id,
        createdBy = persistent.createdBy,
        modifiedBy = persistent.modifiedBy,
        timeOfCreation = persistent.timeOfCreation,
        timeOfModification = persistent.timeOfModification,

        emailAddress = emailAddress,
        personId = person?.persistent?.id,
        username = username ?: "na",
        userType = UserDao.UserType.entries.firstOrNull { it.name == usertype.name }
            ?: error(message = "Unknown UserType: $usertype"),
    )

    fun toUserDto() = UserDto(
        persistentDto = persistent.toPersistentDto(),
        emailAddress = emailAddress,
        person = person?.toPersonDto(),
        username = username,
        userType = (usertype == Usertype.ADMIN).whenTrue { UserType.ACTIVE } ?: UserType.valueOf(usertype.name)
    )

    enum class Usertype {
        ADMIN, ACTIVE, INACTIVE
    }
}

@JvmRecord
data class CreateUser(
    val addressId: UUID?,
    val personId: UUID?,
    val username: String,
    val firstName: String?,
    val surname: String,
    val description: String?,
    val emailAddress: String?,

    val addressLine1: String?,
    val addressLine2: String?,
    val addressLine3: String?,
    val zipCode: String?,
    val city: String?,
    val language: String?,
    val country: String?
) {
    fun toUserDao() = UserDao(
        createdBy = username,
        modifiedBy = username,
        timeOfCreation = LocalDateTime.now(),
        timeOfModification = LocalDateTime.now(),

        emailAddress = emailAddress,
        personId = personId,
        username = username,
        userType = UserDao.UserType.ACTIVE
    )
}

object Users : UUIDTable(name = "T_USER", columnName = "ID") {
    val createdBy = text("CREATED_BY")
    val modifiedBy = text("UPDATED_BY")
    val timeOfCreation = datetime("CREATION_TIME")
    val timeOfModification = datetime("UPDATED_TIME")

    val emailAddress = text("EMAIL").nullable()
    val username = text("USER_NAME")
    val personId = uuid("PERSON_ID").references(People.id)
    val userType = text("USER_TYPE")
    val inactiveSince = datetime("INACTIVE_SINCE").nullable()
}

interface UserRepository {
    fun contains(username: String): Boolean
    fun delete(user: UserDao)
    fun findAll(): List<UserDao>
    fun findById(id: UUID): UserDao?
    fun findByPersonId(id: UUID): List<UserDao>
    fun findByUsername(username: String): UserDao?
    fun findUsernames(userType: List<UserDao.UserType>): List<String>
    fun save(userDao: UserDao): UserDao
}

@Repository
class UserRepositoryImpl : UserRepository by UserRepositoryObject

object UserRepositoryObject : UserRepository {
    override fun contains(username: String): Boolean = transaction {
        Users
            .select(column = Users.username)
            .andWhere { Users.username eq username }
            .count() > 0
    }

    override fun delete(user: UserDao): Unit = transaction {
        Users.deleteWhere { Users.id eq user.id }
    }

    override fun findAll(): List<UserDao> = transaction {
        Users.selectAll().map { it.toUserDao() }
    }

    override fun findById(id: UUID): UserDao? = transaction {
        Users.selectAll()
            .andWhere { Users.id eq id }
            .map { it.toUserDao() }
            .singleOrNull()
    }

    override fun findByPersonId(id: UUID): List<UserDao> = transaction {
        Users.selectAll()
            .andWhere { Users.personId eq id }
            .map { it.toUserDao() }
    }

    override fun findByUsername(username: String): UserDao? = transaction {
        Users
            .selectAll()
            .andWhere { Users.username eq username }
            .singleOrNull()
            ?.toUserDao()

    }

    override fun findUsernames(userType: List<UserDao.UserType>): List<String> = transaction {
        val userTypes = userType.map { it.name }

        Users.select(column = Users.username)
            .andWhere { Users.userType inList userTypes }
            .withDistinct()
            .map { it[Users.username] }
    }

    override fun save(userDao: UserDao): UserDao = when (userDao.isPersisted) {
        true -> update(user = userDao)
        false -> insert(user = userDao)
    }

    private fun insert(user: UserDao): UserDao = transaction {
        Users.insertAndGetId { row ->
            row[createdBy] = user.createdBy
            row[modifiedBy] = user.modifiedBy
            row[timeOfCreation] = user.timeOfCreation
            row[timeOfModification] = user.timeOfModification
            row[emailAddress] = user.emailAddress
            row[username] = user.username
            row[personId] = requireNotNull(user.personId) { "Person id cannot be null when inserting a user." }
            row[userType] = user.userType.name
        }.value.let { newId -> user.also { it.id = newId } }
    }

    private fun update(user: UserDao): UserDao = transaction {
        Users.update(where = { Users.id eq user.id }) {
            it[modifiedBy] = user.modifiedBy
            it[timeOfModification] = user.timeOfModification
            it[emailAddress] = user.emailAddress
            it[username] = user.username
            it[userType] = userType.name
            it[personId] = personId
            // createdBy & timeOfCreation are intentionally not updated
        }.let { user }
    }

    private fun ResultRow.toUserDao(id: UUID? = null): UserDao = UserDao(
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
    private val blogRelations = DaoRelations(fetchRelations = JactorPersistenceRepositiesConfig.fetchBlogRelations)
    private val guestBookRelation = DaoRelation(
        fetchRelation = JactorPersistenceRepositiesConfig.fetchGuestBookRelation
    )

    private val personRelation = DaoRelation(
        fetchRelation = JactorPersistenceRepositiesConfig.fetchPersonRelation,
    )

    val personDao: PersonDao? get() = personRelation.fetchRelatedInstance(id = personId)
    val guestBook: GuestBookDao? get() = guestBookRelation.fetchRelatedInstance(id = id)
    val blogs: List<BlogDao> get() = id?.let { blogRelations.fetchRelations(id = it) } ?: emptyList()

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
