package com.github.jactor.persistence.common

import java.time.LocalDateTime
import java.util.UUID

interface PersistentDao<T> {
    val isPersisted: Boolean get() = id != null
    val isNotPersisted: Boolean get() = !isPersisted

    var id: UUID?
    val createdBy: String
    val modifiedBy: String
    val timeOfCreation: LocalDateTime
    val timeOfModification: LocalDateTime

    fun copyWithoutId(): T
    fun isIdNull(): Boolean = id == null
    fun modifiedBy(modifier: String): T
    fun toPersistent() = Persistent(
        id = id,
        createdBy = createdBy,
        modifiedBy = modifiedBy,
        timeOfCreation = timeOfCreation,
        timeOfModification = timeOfModification
    )
}
