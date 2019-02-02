package com.gitlab.jactor.persistence.dto

import java.time.LocalDateTime

data class UserDto(
        var id: Long? = null,
        var createdBy: String? = null,
        var creationTime: LocalDateTime? = null,
        var updatedBy: String? = null,
        var updatedTime: LocalDateTime? = null,
        var person: PersonDto? = null,
        var emailAddress: String? = null,
        var username: String? = null
) : AsPersistentDto {
    constructor(
            persistent: PersistentDto,
            person: PersonDto?, emailAddress: String?, username: String?
    ) : this(
            persistent.id, persistent.createdBy, persistent.creationTime, persistent.updatedBy, persistent.updatedTime,
            person, emailAddress, username
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
