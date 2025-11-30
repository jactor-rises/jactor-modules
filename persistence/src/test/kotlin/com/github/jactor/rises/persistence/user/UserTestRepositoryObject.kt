package com.github.jactor.rises.persistence.user

import com.github.jactor.rises.persistence.util.toUserDao
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll

object UserTestRepositoryObject {
    fun delete(user: UserDao) {
        Users.deleteWhere { Users.id eq user.id }
    }

    fun findAll(): List<UserDao> = Users.selectAll().map { it.toUserDao() }
}
