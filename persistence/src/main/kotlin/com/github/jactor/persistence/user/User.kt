package com.github.jactor.persistence.user

import com.github.jactor.persistence.Persistent
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
import com.github.jactor.shared.whenTrue
import java.util.UUID

@JvmRecord
data class User(
    val persistent: Persistent = Persistent(),
    val emailAddress: String?,
    val personId: UUID?,
    val username: String?,
    val usertype: Usertype,
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
        userType = UserDao.UserType.entries.firstOrNull { it.name == usertype.name }
            ?: error(message = "Unknown UserType: $usertype"),
    )

    fun toUserDto() = UserDto(
        persistentDto = persistent.toPersistentDto(),
        emailAddress = emailAddress,
        personId = personId,
        username = username,
        userType = (usertype == Usertype.ADMIN).whenTrue { UserType.ACTIVE } ?: UserType.valueOf(usertype.name)
    )

    enum class Usertype {
        ADMIN, ACTIVE, INACTIVE
    }
}
