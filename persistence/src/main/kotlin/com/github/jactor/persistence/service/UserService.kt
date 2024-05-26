package com.github.jactor.persistence.service

import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.jactor.persistence.api.controller.toModel
import com.github.jactor.persistence.dto.UserModel
import com.github.jactor.persistence.entity.UserEntity
import com.github.jactor.persistence.person.PersonService
import com.github.jactor.persistence.repository.UserRepository
import com.github.jactor.shared.api.CreateUserCommand

@Service
class UserService(
    private val personService: PersonService,
    private val userRepository: UserRepository
) {
    fun find(username: String): UserModel? {
        return userRepository.findByUsername(username)
            .map { it.toModel() }
            .orElse(null)
    }

    fun find(id: UUID): UserModel? {
        return userRepository.findById(id)
            .map { it.toModel() }
            .orElse(null)
    }

    @Transactional
    fun update(userModel: UserModel): UserModel? {
        val uuid = userModel.persistentModel.id ?: throw IllegalArgumentException("User must have an id!")
        return userRepository.findById(uuid)
            .map { it.update(userModel) }
            .map { it.toModel() }
            .orElse(null)
    }

    fun create(createUserCommand: CreateUserCommand): UserModel {
        val user = createNewFrom(createUserCommand)

        if (user.id == null) {
            user.id = UUID.randomUUID()
        }

        return userRepository.save(user).toModel()
    }

    private fun createNewFrom(createUserCommand: CreateUserCommand): UserEntity {
        val personModel = createUserCommand.toPersonDto().toModel()
        val personEntity = personService.createWhenNotExists(person = personModel)
        val userEntity = UserEntity(user = createUserCommand.toUserDto().toModel())

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
