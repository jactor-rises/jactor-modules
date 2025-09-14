package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.Objects
import java.util.Optional
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.springframework.data.repository.CrudRepository
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
import com.github.jactor.persistence.Config.ioContext
import com.github.jactor.persistence.common.PersistentDao
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
import com.github.jactor.shared.whenTrue
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

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
    ): ResponseEntity<UserDto> = when (userService.isAlreadyPresent(createUserCommand.username)) {
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
        false -> userService.update(user = User(userDto = userDto))?.let {
            ResponseEntity(it.toDto(), HttpStatus.ACCEPTED)
        } ?: ResponseEntity(HttpStatus.BAD_REQUEST)
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
class UserService(
    private val personService: PersonService,
    private val userRepository: UserRepository
) {
    suspend fun find(username: String): User? = ioContext {
        userRepository.findByUsername(username).getOrNull()?.toModel()
    }

    suspend fun find(id: UUID): User? = ioContext {
        userRepository.findById(id).getOrNull()?.toPerson()
    }

    @Transactional
    suspend fun update(user: User): User? {
        val uuid = user.persistent.id ?: throw IllegalArgumentException("User must have an id!")
        return ioContext {
            userRepository.findById(uuid).map { it.update(user) }
                .getOrNull()?.toPerson()
        }
    }

    suspend fun create(createUserCommand: CreateUserCommand): User {
        val user = createNewFrom(createUserCommand)

        if (user.id == null) {
            user.id = UUID.randomUUID()
        }

        return ioContext { userRepository.save(user).toPerson() }
    }

    private suspend fun createNewFrom(createUserCommand: CreateUserCommand): UserDao {
        val person = createUserCommand.toPersonDto().toPerson()
        val personEntity = ioContext { personService.createWhenNotExists(person = person) }
        val user = UserDao(user = createUserCommand.toUserDto().toUser())
        user.personDao = personEntity

        return user
    }

    suspend fun findUsernames(userType: UserDao.UserType): List<String> = ioContext {
        userRepository.findByUserTypeIn(listOf(userType))
            .map { it.username ?: "username of user with id '${it.id} is null!" }
    }

    suspend fun isAlreadyPresent(username: String): Boolean = ioContext {
        userRepository.findByUsername(username).isPresent
    }
}

@JvmRecord
data class User(
    val persistent: Persistent = Persistent(),
    val person: Person?,
    val emailAddress: String?,
    val username: String?,
    val usertype: Usertype,
) {
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
        persistent = Persistent(userDto.persistentDto),
        person = userDto.person?.let { Person(personDto = it) },
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

    fun withId(): User = copy(persistent = persistent.copy(id = persistent.id ?: UUID.randomUUID()))
    fun toEntity() = UserDao(user = this)

    enum class Usertype {
        ADMIN, ACTIVE, INACTIVE
    }
}

object Users: UUIDTable(name = "T_USER", columnName = "ID") {
}

interface UserRepository : CrudRepository<UserDao, UUID> {
    fun findByUsername(username: String): Optional<UserDao>
    fun findByUserTypeIn(userType: Collection<UserDao.UserType>): List<UserDao>
}

@Entity
@Table(name = "T_USER")
class UserDao : PersistentDao<UserDao?> {
    @Id
    override var id: UUID? = null

    @Embedded
    @AttributeOverride(name = "createdBy", column = Column(name = "CREATED_BY"))
    @AttributeOverride(name = "timeOfCreation", column = Column(name = "CREATION_TIME"))
    @AttributeOverride(name = "modifiedBy", column = Column(name = "UPDATED_BY"))
    @AttributeOverride(name = "timeOfModification", column = Column(name = "UPDATED_TIME"))
    lateinit var persistentDataEmbeddable: PersistentDataEmbeddable
        internal set

    @Column(name = "EMAIL")
    var emailAddress: String? = null

    @Column(name = "USER_NAME", nullable = false)
    var username: String? = null

    @JoinColumn(name = "PERSON_ID")
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var personDao: PersonDao? = null
        internal set

    @OneToOne(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var guestBook: GuestBookDao? = null
        internal set(value) {
            value?.let { it.user = this }
            field = value
        }

    @OneToMany(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    private var blogs: MutableSet<BlogDao> = HashSet()

    @Column(name = "USER_TYPE")
    @Enumerated(EnumType.STRING)
    private var userType: UserType? = null

    constructor() {
        // used by entity manager
    }

    /**
     * @param userEntity is used to create an entity
     */
    private constructor(userEntity: UserDao) {
        blogs = userEntity.blogs.map { it.copyWithoutId() }.toMutableSet()
        emailAddress = userEntity.emailAddress
        guestBook = userEntity.guestBook?.copyWithoutId()
        id = userEntity.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        personDao = userEntity.personDao?.copyWithoutId()
        username = userEntity.username
        userType = userEntity.userType
    }

    constructor(user: User) {
        addValues(user)
    }

    private fun addValues(user: User) {
        emailAddress = user.emailAddress
        id = user.persistent.id
        persistentDataEmbeddable = PersistentDataEmbeddable(user.persistent)
        personDao = user.person?.let { PersonDao(it) }
        username = user.username
        userType = UserType.entries
            .firstOrNull { aUserType: UserType -> aUserType.name == user.usertype.name }
            ?: throw IllegalArgumentException("Unknown UserType: " + user.usertype)
    }

    fun toModel() = User(
        persistent = persistentDataEmbeddable.toModel(id),
        person = personDao?.toModel(),
        emailAddress = emailAddress,
        username = username,
        usertype = userType?.let { User.Usertype.valueOf(it.name) } ?: User.Usertype.INACTIVE,
    )

    fun fetchPerson(): PersonDao {
        personDao?.addUser(this)
        return personDao ?: error("No person provided to the user entity")
    }

    override fun copyWithoutId(): UserDao {
        val user = UserDao(this)
        user.id = null
        return user
    }

    override fun modifiedBy(modifier: String): UserDao {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    fun add(blogEntity: BlogDao) {
        blogs.add(blogEntity)
        blogEntity.user = this
    }

    fun update(user: User): UserDao {
        addValues(user)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return other === this || other != null && javaClass == other.javaClass &&
            emailAddress == (other as UserDao).emailAddress &&
            personDao == other.personDao &&
            username == other.username
    }

    override fun hashCode(): Int {
        return Objects.hash(username, personDao, emailAddress)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .appendSuper(super.toString())
            .append(username)
            .append(emailAddress)
            .append(blogs)
            .append("guestbook.id=" + if (guestBook != null) guestBook!!.id else null)
            .append(personDao)
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

    fun getBlogs(): Set<BlogDao> {
        return blogs
    }

    enum class UserType {
        ADMIN, ACTIVE, INACTIVE
    }
}
