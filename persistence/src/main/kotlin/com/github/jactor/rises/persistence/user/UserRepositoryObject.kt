package com.github.jactor.rises.persistence.user

import com.github.jactor.rises.persistence.util.toUserDao
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

object UserRepositoryObject : UserRepository {
    override fun contains(username: String): Boolean = Users
        .select(column = Users.username)
        .andWhere { Users.username eq username }
        .count() > 0

    override fun findById(id: UUID): UserDao? = Users.selectAll()
        .andWhere { Users.id eq id }
        .map { it.toUserDao() }
        .singleOrNull()

    override fun findByPersonId(id: UUID): List<UserDao> = Users.selectAll()
        .andWhere { Users.personId eq id }
        .map { it.toUserDao() }

    override fun findByUsername(username: String): UserDao? = Users
        .selectAll()
        .andWhere { Users.username eq username }
        .singleOrNull()
        ?.toUserDao()

    override fun findUsernames(userType: List<UserType>): List<String> = run {
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

    private fun insert(user: UserDao): UserDao = Users.insertAndGetId { row ->
        row[createdBy] = user.createdBy
        row[modifiedBy] = user.modifiedBy
        row[timeOfCreation] = user.timeOfCreation
        row[timeOfModification] = user.timeOfModification
        row[emailAddress] = user.emailAddress
        row[username] = user.username
        row[personId] = requireNotNull(user.personId) { "Person id cannot be null when inserting a user." }
        row[userType] = user.userType.name
    }.value.let { newId -> user.also { it.id = newId } }

    private fun update(user: UserDao): UserDao = Users.update(
        where = { Users.id eq user.id },
    ) {
        it[modifiedBy] = user.modifiedBy
        it[timeOfModification] = user.timeOfModification
        it[emailAddress] = user.emailAddress
        it[username] = user.username
        it[userType] = userType.name
        it[personId] = personId
        // createdBy & timeOfCreation are intentionally not updated
    }.let { user }
}
