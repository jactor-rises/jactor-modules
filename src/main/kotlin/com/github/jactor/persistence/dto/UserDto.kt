package com.github.jactor.persistence.dto

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
        if (persistentDto == null) {
            persistentDto = PersistentDto()
        }

        return persistentDto as PersistentDto
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long?) {
        persistentDto = setPersistentId(id)
    }
}
