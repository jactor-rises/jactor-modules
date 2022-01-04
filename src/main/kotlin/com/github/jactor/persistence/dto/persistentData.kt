package com.github.jactor.persistence.dto

open class PersistentData(
    private var persistentDto: PersistentDto?
) {
    var id: Long?
        get() = persistentDto?.id
        set(value) {
            if (persistentDto == null) {
                persistentDto = PersistentDto()
            }

            persistentDto!!.id = value
        }

    fun fetchPersistentDto(): PersistentDto {
        return if (persistentDto != null) persistentDto!! else PersistentDto()
    }
}
