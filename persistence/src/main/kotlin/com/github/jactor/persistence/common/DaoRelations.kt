package com.github.jactor.persistence.common

class DaoRelations<T : PersistentDao<*>>(private val fetchRelations: (PersistentDao<*>) -> List<T>) {
    fun fetchRelationsTo(persistentDao: PersistentDao<*>): List<T> = fetchRelations.invoke(persistentDao)
}
