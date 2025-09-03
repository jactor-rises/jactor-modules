package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.Objects
import java.util.Optional
import java.util.UUID
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
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
import com.github.jactor.persistence.common.PersistentDataEmbeddable
import com.github.jactor.persistence.common.PersistentEntity
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.shared.api.CreateUserCommand
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
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
    fun find(@PathVariable("username") username: String): ResponseEntity<UserDto> {
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
    operator fun get(@PathVariable("id") id: UUID): ResponseEntity<UserDto> {
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
    fun post(@RequestBody createUserCommand: CreateUserCommand): ResponseEntity<UserDto> {
        if (userService.isAlreadyPresent(createUserCommand.username)) {
            return ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(userService.create(createUserCommand).toDto(), HttpStatus.CREATED)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "User updated"),
            ApiResponse(responseCode = "400", description = "Did not find user with id or no body is present")
        ]
    )
    @Operation(description = "Update a user by its id")
    @PutMapping("/update")
    fun put(@RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        if (userDto.harIkkeIdentifikator()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val updatedUser = userService.update(
            user = User(userDto = userDto)
        )

        return updatedUser?.let { ResponseEntity(it.toDto(), HttpStatus.ACCEPTED) }
            ?: ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ApiResponses(ApiResponse(responseCode = "200", description = "List of usernames found"))
    @GetMapping("/usernames")
    @Operation(description = "Find all usernames for a user type")
    fun findAllUsernames(
        @RequestParam(required = false, defaultValue = "ACTIVE") userType: String
    ): ResponseEntity<List<String>> {
        return ResponseEntity(userService.findUsernames(UserEntity.UserType.valueOf(userType)), HttpStatus.OK)
    }
}

@Service
class UserService(
    private val personService: PersonService,
    private val userRepository: UserRepository
) {
    fun find(username: String): User? {
        return userRepository.findByUsername(username)
            .map { it.toModel() }
            .orElse(null)
    }

    fun find(id: UUID): User? {
        return userRepository.findById(id)
            .map { it.toModel() }
            .orElse(null)
    }

    @Transactional
    fun update(user: User): User? {
        val uuid = user.persistent.id ?: throw IllegalArgumentException("User must have an id!")
        return userRepository.findById(uuid)
            .map { it.update(user) }
            .map { it.toModel() }
            .orElse(null)
    }

    fun create(createUserCommand: CreateUserCommand): User {
        val user = createNewFrom(createUserCommand)

        if (user.id == null) {
            user.id = UUID.randomUUID()
        }

        return userRepository.save(user).toModel()
    }

    private fun createNewFrom(createUserCommand: CreateUserCommand): UserEntity {
        val person = createUserCommand.toPersonDto().toModel()
        val personEntity = personService.createWhenNotExists(person = person)
        val user = UserEntity(user = createUserCommand.toUserDto().toModel())
        user.person = personEntity

        return user
    }

    fun findUsernames(userType: UserEntity.UserType): List<String> {
        return userRepository.findByUserTypeIn(listOf(userType))
            .map { it.username ?: "username of user with id '${it.id} is null!" }
    }

    fun isAlreadyPresent(username: String): Boolean {
        return userRepository.findByUsername(username).isPresent
    }
}

@JvmRecord
data class User(
    val persistent: Persistent = Persistent(),
    val person: Person? = null,
    val emailAddress: String? = null,
    val username: String? = null,
    val usertype: Usertype = Usertype.ACTIVE
) {
    constructor(persistent: Persistent, user: User) : this(
        persistent = persistent,
        emailAddress = user.emailAddress,
        person = user.person,
        username = user.username
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
        persistentDto = persistent.toDto(),
        emailAddress = emailAddress,
        person = person?.toPersonDto(),
        username = username,
        userType = if (usertype == Usertype.ADMIN) UserType.ACTIVE else UserType.valueOf(usertype.name)
    )

    enum class Usertype {
        ADMIN, ACTIVE, INACTIVE
    }
}

internal object UserBuilder {
    fun new(userDto: User): UserData = UserData(
        userDto = userDto.copy(persistent = userDto.persistent.copy(id = UUID.randomUUID()))
    )

    fun unchanged(user: User): UserData = UserData(
        userDto = user
    )

    @JvmRecord
    data class UserData(val userDto: User) {
        fun build(): UserEntity = UserEntity(user = userDto)
    }
}

interface UserRepository : CrudRepository<UserEntity, UUID> {
    fun findByUsername(username: String): Optional<UserEntity>
    fun findByUserTypeIn(userType: Collection<UserEntity.UserType>): List<UserEntity>
}

@Entity
@Table(name = "T_USER")
class UserEntity : PersistentEntity<UserEntity?> {
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
    var person: PersonEntity? = null
        internal set(value) {
            field = value
        }

    @OneToOne(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var guestBook: GuestBookEntity? = null
        internal set(value) {
            value?.let { it.user = this }
            field = value
        }

    @OneToMany(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    private var blogs: MutableSet<BlogEntity> = HashSet()

    @Column(name = "USER_TYPE")
    @Enumerated(EnumType.STRING)
    private var userType: UserType? = null

    constructor() {
        // used by entity manager
    }

    /**
     * @param userEntity is used to create an entity
     */
    private constructor(userEntity: UserEntity) {
        blogs = userEntity.blogs.map { it.copyWithoutId() }.toMutableSet()
        emailAddress = userEntity.emailAddress
        guestBook = userEntity.guestBook?.copyWithoutId()
        id = userEntity.id
        persistentDataEmbeddable = PersistentDataEmbeddable()
        person = userEntity.person?.copyWithoutId()
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
        person = user.person?.let { PersonEntity(it) }
        username = user.username
        userType = UserType.entries
            .firstOrNull { aUserType: UserType -> aUserType.name == user.usertype.name }
            ?: throw IllegalArgumentException("Unknown UserType: " + user.usertype)
    }

    fun toModel(): User {
        return User(
            persistent = persistentDataEmbeddable.toModel(id),
            person = person?.toModel(),
            emailAddress = emailAddress,
            username = username
        )
    }

    fun fetchPerson(): PersonEntity {
        person?.addUser(this)
        return person ?: error("No person provided to the user entity")
    }

    override fun copyWithoutId(): UserEntity {
        val user = UserEntity(this)
        user.id = null
        return user
    }

    override fun modifiedBy(modifier: String): UserEntity {
        persistentDataEmbeddable.modifiedBy(modifier)
        return this
    }

    fun add(blogEntity: BlogEntity) {
        blogs.add(blogEntity)
        blogEntity.user = this
    }

    fun update(user: User): UserEntity {
        addValues(user)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return other === this || other != null && javaClass == other.javaClass &&
            emailAddress == (other as UserEntity).emailAddress &&
            person == other.person &&
            username == other.username
    }

    override fun hashCode(): Int {
        return Objects.hash(username, person, emailAddress)
    }

    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .appendSuper(super.toString())
            .append(username)
            .append(emailAddress)
            .append(blogs)
            .append("guestbook.id=" + if (guestBook != null) guestBook!!.id else null)
            .append(person)
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

    fun getBlogs(): Set<BlogEntity> {
        return blogs
    }

    enum class UserType {
        ADMIN, ACTIVE, INACTIVE
    }
}
