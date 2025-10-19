package com.github.jactor.persistence.common

import java.util.UUID
import com.github.jactor.shared.whenTrue

class DaoRelation<T : PersistentDao<*>>(private val fetchRelation: (UUID) -> T?) {
    private var relation: T? = null

    fun fetchRelatedInstance(id: UUID?): T? = relation?.let { initedRelation ->
        id?.let { initedId -> (initedRelation.id == initedId).whenTrue { initedRelation } }
    } ?: id?.let { initedId -> fetchRelation.invoke(initedId).also { newRelation -> this.relation = newRelation } }
}
