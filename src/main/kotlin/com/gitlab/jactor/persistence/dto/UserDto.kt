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
            user: UserDto
    ) : this(
            user.id, user.createdBy, user.creationTime, user.updatedBy, user.updatedTime,
            user.person, user.emailAddress, user.username
    )

    override fun asPersistentDto(): PersistentDto {
        return PersistentDto(id, createdBy, creationTime, updatedBy, updatedTime)
    }
}
