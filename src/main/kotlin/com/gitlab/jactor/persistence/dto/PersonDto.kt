package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class PersonDto(
        private var id: Long? = null,
        private var createdBy: String? = null,
        private var creationTime: LocalDateTime? = null,
        private var updatedBy: String? = null,
        private var updatedTime: LocalDateTime? = null,
        var address: AddressDto? = null,
        var locale: String? = null,
        var firstName: String? = null,
        var surname: String? = null,
        var description: String? = null
) : AsPersistentDto {
    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
