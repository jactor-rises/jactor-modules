package com.github.jactor.persistence.entity

import java.time.LocalDateTime
import java.util.UUID

interface PersistentEntity<T> {
    var id: UUID?
    val createdBy: String
    val modifiedBy: String
    val timeOfCreation: LocalDateTime
    val timeOfModification: LocalDateTime

    fun copyWithoutId(): T
    fun modifiedBy(modifier: String): T
}
