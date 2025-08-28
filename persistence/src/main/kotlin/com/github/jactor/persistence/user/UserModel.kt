package com.github.jactor.persistence.user

import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.PersonModel
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType

@JvmRecord
data class UserModel(
    val persistentModel: PersistentModel = PersistentModel(),
    val person: PersonModel? = null,
    val emailAddress: String? = null,
    val username: String? = null,
    val usertype: Usertype = Usertype.ACTIVE
) {
    constructor(persistent: PersistentModel, userModel: UserModel) : this(
        persistentModel = persistent,
        emailAddress = userModel.emailAddress,
        person = userModel.person,
        username = userModel.username
    )

    constructor(
        persistentModel: PersistentModel,
        personInternal: PersonModel?,
        emailAddress: String?,
        username: String?
    ) : this(
        persistentModel = persistentModel,
        person = personInternal,
        emailAddress = emailAddress,
        username = username,
        usertype = Usertype.ACTIVE
    )

    constructor(userDto: UserDto) : this(
        persistentModel = PersistentModel(userDto.persistentDto),
        person = if (userDto.person != null) PersonModel(userDto.person!!) else null,
        emailAddress = userDto.emailAddress,
        username = userDto.username,
        usertype = Usertype.valueOf(userDto.userType.name)
    )

    fun toDto() = UserDto(
        persistentDto = persistentModel.toDto(),
        emailAddress = emailAddress,
        person = person?.toPersonDto(),
        username = username,
        userType = if (usertype == Usertype.ADMIN) UserType.ACTIVE else UserType.valueOf(usertype.name)
    )

    enum class Usertype {
        ADMIN, ACTIVE, INACTIVE
    }
}
