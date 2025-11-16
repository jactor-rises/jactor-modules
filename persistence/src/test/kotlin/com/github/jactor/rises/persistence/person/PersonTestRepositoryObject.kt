package com.github.jactor.rises.persistence.person

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import com.github.jactor.rises.persistence.guestbook.toPersonDao

object PersonTestRepositoryObject {
    fun findAll(): List<PersonDao> = People.selectAll().map { it.toPersonDao() }
    fun findBySurname(surname: String?): List<PersonDao> = when {
        (surname?.isBlank() ?: true) -> emptyList()

        else -> People
            .selectAll()
            .andWhere { People.surname eq surname }
            .map { it.toPersonDao() }
    }
}