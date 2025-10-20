package com.github.jactor.persistence.user

import java.util.UUID
import org.springframework.stereotype.Repository

interface UserRepository {
    fun contains(username: String): Boolean
    fun delete(user: UserDao)
    fun findAll(): List<UserDao>
    fun findById(id: UUID): UserDao?
    fun findByPersonId(id: UUID): List<UserDao>
    fun findByUsername(username: String): UserDao?
    fun findUsernames(userType: List<UserType>): List<String>
    fun save(userDao: UserDao): UserDao
}

@Repository
class UserRepositoryImpl : UserRepository by UserRepositoryObject
