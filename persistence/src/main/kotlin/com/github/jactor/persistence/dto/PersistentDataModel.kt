package com.github.jactor.persistence.dto

import java.util.UUID

open class PersistentDataModel(open val persistentDto: PersistentDto) {
    var id: UUID?
        get() = persistentDto.id
        set(value) {
            persistentDto.id = value
        }
}
