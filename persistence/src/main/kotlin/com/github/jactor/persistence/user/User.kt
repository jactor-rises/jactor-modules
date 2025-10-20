package com.github.jactor.persistence.user

import com.github.jactor.persistence.Persistent
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.whenTrue
import java.util.UUID

@JvmRecord
data class User(
    val persistent: Persistent = Persistent(),
    val emailAddress: String?,
    val personId: UUID?,
    val username: String?,
    val userType: UserType,
) {
    val id: UUID
        get() = persistent.id ?: error("User is not persisted!")

    fun toUserDao() = UserDao(
        id = persistent.id,
        createdBy = persistent.createdBy,
        modifiedBy = persistent.modifiedBy,
        timeOfCreation = persistent.timeOfCreation,
        timeOfModification = persistent.timeOfModification,

        emailAddress = emailAddress,
        personId = personId,
        username = username ?: "na",
        userType = UserType.entries.firstOrNull { it.name == userType.name }
            ?: error(message = "Unknown UserType: $userType"),
    )

    fun toUserDto() = UserDto(
        persistentDto = persistent.toPersistentDto(),
        emailAddress = emailAddress,
        personId = personId,
        username = username,
        userType = (userType == UserType.ADMIN).whenTrue { UserTypeDto.ACTIVE }
            ?: UserTypeDto.valueOf(userType.name)
    )
}
