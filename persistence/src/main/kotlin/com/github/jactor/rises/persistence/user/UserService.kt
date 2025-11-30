package com.github.jactor.rises.persistence.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository = UserRepositoryObject) {
    suspend fun find(username: String): User? = userRepository.findByUsername(username)?.toUser()
    suspend fun find(id: UUID): User? = userRepository.findById(id = id)?.toUser()

    @Transactional
    suspend fun update(user: User): User = userRepository.save(userDao = user.toUserDao()).toUser()
    suspend fun create(createUser: CreateUser): User = userRepository.save(userDao = createUser.toUserDao()).toUser()
    suspend fun findUsernames(userType: UserType): List<String> = userRepository.findUsernames(
        userType = listOf(userType),
    )

    suspend fun isAlreadyPersisted(username: String): Boolean = userRepository.contains(username)
}
