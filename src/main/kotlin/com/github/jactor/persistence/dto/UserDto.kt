package com.gitlab.jactor.persistence.dto

data class UserDto(
        var persistentDto: PersistentDto? = null,
        var person: PersonDto? = null,
        var emailAddress: String? = null,
        var username: String? = null
) : Persistent {
    constructor(
            persistent: PersistentDto, user: UserDto
    ) : this(
            persistent, user.person, user.emailAddress, user.username
    )

    override fun fetchPersistentDto(): PersistentDto {
        return if (persistentDto != null) persistentDto as PersistentDto else PersistentDto()
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long?) {
        persistentDto = setPersistentId(id)
    }
}
