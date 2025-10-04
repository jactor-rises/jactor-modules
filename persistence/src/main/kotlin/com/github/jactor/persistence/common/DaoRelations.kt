package com.github.jactor.persistence.common

import java.util.UUID

class DaoRelations<T : PersistentDao<*>>(private val fetchRelations: (UUID) -> List<T>) {
    fun fetchRelations(id: UUID): List<T> = fetchRelations.invoke(id)
}
