package com.github.jactor.persistence.service

import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.jactor.persistence.api.command.CreateUserCommand
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.UserEntity
import com.github.jactor.persistence.repository.UserRepository

@Service
class UserService(
    private val personService: PersonService,
    private val userRepository: UserRepository
) {
    fun find(username: String): UserInternalDto? {
        return userRepository.findByUsername(username)
            .map { it.asDto() }
            .orElse(null)
    }

    fun find(id: UUID): UserInternalDto? {
        return userRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    @Transactional
    fun update(userInternalDto: UserInternalDto): UserInternalDto? {
        return userRepository.findById(userInternalDto.id ?: throw IllegalArgumentException("User must have an id!"))
            .map { it.update(userInternalDto) }
            .map { it.asDto() }
            .orElse(null)
    }

    fun create(createUserCommand: CreateUserCommand): UserInternalDto {
        val user = createNewFrom(createUserCommand)

        if (user.id == null) {
            user.id = UUID.randomUUID()
        }

        return userRepository.save(user).asDto()
    }

    private fun createNewFrom(createUserCommand: CreateUserCommand): UserEntity {
        val personDto = createUserCommand.fetchPersonDto()
        val personEntity = personService.createWhenNotExists(personDto)
        val userEntity = UserEntity(createUserCommand.fetchUserDto())

        userEntity.setPersonEntity(personEntity)

        return userEntity
    }

    fun findUsernames(userType: UserEntity.UserType): List<String> {
        return userRepository.findByUserTypeIn(listOf(userType))
            .map { it.username ?: "username of user with id '${it.id} is null!" }
    }

    fun isAlreadyPresent(username: String): Boolean {
        return userRepository.findByUsername(username).isPresent
    }
}
