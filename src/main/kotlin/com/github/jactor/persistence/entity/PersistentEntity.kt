package com.github.jactor.persistence.entity

interface PersistentEntity<T> : PersistentData {
    fun copyWithoutId(): T
    fun modify()
    var id: Long?
}
