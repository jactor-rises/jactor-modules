package com.github.jactor.persistence.entity

interface PersistentEntity<T> : PersistentData {
    var id: Long?

    fun copyWithoutId(): T
    fun modifiedBy(modifier: String): T
}
