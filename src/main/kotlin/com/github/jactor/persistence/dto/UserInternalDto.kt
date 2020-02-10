package com.github.jactor.persistence.dto

data class UserInternalDto(
        var persistentDto: PersistentDto? = null,
        var personInternal: PersonInternalDto? = null,
        var emailAddress: String? = null,
        var username: String? = null,
        var usertype: Usertype = Usertype.ACTIVE
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, userInternal: UserInternalDto
    ) : this(
            persistent, userInternal.personInternal, userInternal.emailAddress, userInternal.username
    )

    constructor(
            persistentDto: PersistentDto?, personInternal: PersonInternalDto?, emailAddress: String?, username: String?
    ) : this(
            persistentDto, personInternal, emailAddress, username, Usertype.ACTIVE
    )
}

enum class Usertype {
    ADMIN, ACTIVE, INACTIVE
}
