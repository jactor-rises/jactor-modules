package com.github.jactor.persistence.dto

open class PersistentData(open val persistentDto: PersistentDto) {
    var id: Long?
        get() = persistentDto.id
        set(value) {
            persistentDto.id = value
        }
}
