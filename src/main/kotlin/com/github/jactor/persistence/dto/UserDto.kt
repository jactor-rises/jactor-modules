package com.github.jactor.persistence.dto

data class UserDto(
        var persistentDto: PersistentDto? = null,
        var person: PersonDto? = null,
        var emailAddress: String? = null,
        var username: String? = null,
        var userType: UserType = UserType.ACTIVE
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, user: UserDto
    ) : this(
            persistent, user.person, user.emailAddress, user.username
    )

    constructor(
            persistentDto: PersistentDto?, person: PersonDto?, emailAddress: String?, username: String?
    ) : this(
            persistentDto, person, emailAddress, username, UserType.ACTIVE
    )
}

enum class UserType {
    ADMIN, ACTIVE, INACTIVE
}
