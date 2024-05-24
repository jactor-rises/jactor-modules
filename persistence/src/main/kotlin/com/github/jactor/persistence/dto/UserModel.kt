package com.github.jactor.persistence.dto

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType

data class UserModel(
    val persistentModel: PersistentModel = PersistentModel(),
    var person: PersonModel? = null,
    var emailAddress: String? = null,
    var username: String? = null,
    var usertype: Usertype = Usertype.ACTIVE
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(
        persistent: PersistentModel, userInternal: UserModel
    ) : this(
        persistent, userInternal.person, userInternal.emailAddress, userInternal.username
    )

    constructor(
        persistentModel: PersistentModel, personInternal: PersonModel?, emailAddress: String?, username: String?
    ) : this(
        persistentModel = persistentModel,
        person = personInternal,
        emailAddress = emailAddress,
        username = username,
        usertype = Usertype.ACTIVE
    )

    constructor(userDto: UserDto) : this(
        persistentModel = PersistentModel(id = userDto.id),
        person = if (userDto.person != null) PersonModel(userDto.person!!) else null,
        emailAddress = userDto.emailAddress,
        username = userDto.username,
        usertype = Usertype.valueOf(userDto.userType.name)
    )

    fun toUserDto() = UserDto(
        id = persistentModel.id,
        emailAddress = emailAddress,
        person = person?.toPersonDto(),
        username = username,
        userType = if (usertype == Usertype.ADMIN) UserType.ACTIVE else UserType.valueOf(usertype.name)
    )

    enum class Usertype {
        ADMIN, ACTIVE, INACTIVE
    }
}
