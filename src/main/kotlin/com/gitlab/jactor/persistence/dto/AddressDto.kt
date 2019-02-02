package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class AddressDto(
        private var id: Long? = null,
        private var createdBy: String? = null,
        private var creationTime: LocalDateTime? = null,
        private var updatedBy: String? = null,
        private var updatedTime: LocalDateTime? = null,
        var zipCode: Int? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var addressLine3: String? = null,
        var city: String? = null,
        var country: String? = null
) : AsPersistentDto {
    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
