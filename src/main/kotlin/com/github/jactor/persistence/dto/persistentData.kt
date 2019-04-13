package com.github.jactor.persistence.dto

open class PersistentData (
        private var persistentDto: PersistentDto?
)  {
    fun fetchPersistentDto(): PersistentDto {
        return if (persistentDto != null) persistentDto!! else PersistentDto()
    }

    fun getId(): Long? {
        return persistentDto?.id
    }

    fun setId(id: Long?) {
        if (persistentDto == null) {
            persistentDto = PersistentDto()
        }

        persistentDto!!.id = id
    }
}
