package com.github.jactor.rises.persistence.user

import com.github.jactor.rises.persistence.PersistentDao
import java.time.LocalDateTime
import java.util.UUID

data class UserDao(
    override var id: UUID? = null,
    override val createdBy: String,
    override val timeOfCreation: LocalDateTime,
    override var modifiedBy: String,
    override var timeOfModification: LocalDateTime,
    internal var userType: UserType = UserType.ACTIVE,
    internal var emailAddress: String? = null,
    internal var personId: UUID? = null,
    internal var username: String = "na",
) : PersistentDao<UserDao> {
    override fun copyWithoutId(): UserDao = copy(
        id = null,
        personId = null,
    )

    override fun modifiedBy(modifier: String): UserDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }

    fun toUser() = User(
        persistent = toPersistent(),
        username = username,
        emailAddress = emailAddress,
        personId = personId,
        userType = UserType.entries.firstOrNull { it.name == userType.name }
            ?: error("Unknown UserType: $userType"),
    )
}
