package com.github.jactor.persistence.user

import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

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

    override fun findUsernames(userType: List<UserType>): List<String> = transaction {
        val userTypes = userType.map { it.name }

        Users.select(column = Users.username)
            .andWhere { Users.userType inList userTypes }
            .withDistinct()
            .map { it[Users.username] }
    }

    override fun save(userDao: UserDao): UserDao = when (userDao.isPersisted) {
        true -> update(user = userDao)
        false -> insert(user = userDao)
    }.also {
        val users = findAll()
        println("${users.size} users persisted: ${users.map { "${it.username}/${it.id}" }}")
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
