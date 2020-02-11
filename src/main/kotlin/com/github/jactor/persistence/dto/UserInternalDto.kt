package com.github.jactor.persistence.dto

import com.github.jactor.shared.dto.UserDto
import com.github.jactor.shared.dto.UserType

data class UserInternalDto(
        var persistentDto: PersistentDto? = null,
        var person: PersonInternalDto? = null,
        var emailAddress: String? = null,
        var username: String? = null,
        var usertype: Usertype = Usertype.ACTIVE
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, userInternal: UserInternalDto
    ) : this(
            persistent, userInternal.person, userInternal.emailAddress, userInternal.username
    )

    constructor(
            persistentDto: PersistentDto?, personInternal: PersonInternalDto?, emailAddress: String?, username: String?
    ) : this(
            persistentDto, personInternal, emailAddress, username, Usertype.ACTIVE
    )

    constructor(userDto: UserDto) : this(
            persistentDto = if (userDto.id != null) PersistentDto(id = userDto.id) else null,

            person = if (userDto.person != null) PersonInternalDto(userDto.person!!) else null,
            emailAddress = userDto.emailAddress,
            username = userDto.username,
            usertype = Usertype.valueOf(userDto.userType.name)
    )

    fun toUserDto() = UserDto(
            id = persistentDto?.id,

            emailAddress = emailAddress,
            person = person?.toPersonDto(),
            username = username,
            userType = if (usertype == Usertype.ADMIN) UserType.ACTIVE else UserType.valueOf(usertype.name)
    )
}

enum class Usertype {
    ADMIN, ACTIVE, INACTIVE
}
