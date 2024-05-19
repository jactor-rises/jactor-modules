package com.github.jactor.persistence.entity

import java.util.UUID

interface PersistentEntity<T> : PersistentData {
    var id: UUID?

    fun copyWithoutId(): T
    fun modifiedBy(modifier: String): T
}
