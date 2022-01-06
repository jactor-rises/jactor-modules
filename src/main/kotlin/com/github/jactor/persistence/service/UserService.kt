package com.github.jactor.persistence.service

import com.github.jactor.persistence.command.CreateUserCommand
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.UserEntity
import com.github.jactor.persistence.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.stream.Collectors

@Service
class UserService(
    private val personService: PersonService,
    private val userRepository: UserRepository
) {
    fun find(username: String): Optional<UserInternalDto> {
        return userRepository.findByUsername(username).map { obj: UserEntity? -> obj?.asDto() }
    }

    fun find(id: Long): Optional<UserInternalDto> {
        return userRepository.findById(id).map { obj: UserEntity? -> obj?.asDto() }
    }

    @Transactional
    fun update(userInternalDto: UserInternalDto): Optional<UserInternalDto> {
        return userRepository.findById(userInternalDto.id ?: throw IllegalArgumentException("User must have an id!"))
            .map<UserEntity> { userEntity: UserEntity? -> userEntity?.update(userInternalDto) }
            .map { obj: UserEntity -> obj.asDto() }
    }

    fun create(createUserCommand: CreateUserCommand): UserInternalDto {
        return userRepository.save(createNewFrom(createUserCommand)).asDto()
    }

    private fun createNewFrom(createUserCommand: CreateUserCommand): UserEntity {
        val personDto = createUserCommand.fetchPersonDto()
        val personEntity = personService.createWhenNotExists(personDto)
        val userEntity = UserEntity(createUserCommand.fetchUserDto())

        userEntity.setPersonEntity(personEntity)

        return userEntity
    }

    fun findUsernames(userType: UserEntity.UserType): List<String> {
        return userRepository.findByUserTypeIn(listOf(userType)).stream()
            .map<String> { obj: UserEntity? -> obj?.username }
            .collect(Collectors.toList())
    }

    fun isAlreadyPresent(username: String): Boolean {
        return userRepository.findByUsername(username).isPresent
    }
}
