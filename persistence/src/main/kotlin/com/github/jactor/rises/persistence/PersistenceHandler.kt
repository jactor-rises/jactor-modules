package com.github.jactor.rises.persistence

import com.github.jactor.rises.shared.whenTrue
import org.springframework.stereotype.Component

@Component
class PersistenceHandler {
    suspend fun <T : PersistentDao<*>> modifyAndSave(
        dao: T,
        modifier: suspend (T) -> T,
    ): T = run {
        dao.isPersisted.whenTrue { dao.modifiedBy(modifier = "todo") }
        modifier(dao)
    }
}
