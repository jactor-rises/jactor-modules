package com.github.jactor.persistence.user

import java.time.LocalDateTime
import java.util.UUID

@JvmRecord
data class CreateUser(
    val addressId: UUID?,
    val personId: UUID?,
    val username: String,
    val firstName: String?,
    val surname: String,
    val description: String?,
    val emailAddress: String?,

    val addressLine1: String?,
    val addressLine2: String?,
    val addressLine3: String?,
    val zipCode: String?,
    val city: String?,
    val language: String?,
    val country: String?
) {
    fun toUserDao() = UserDao(
        createdBy = username,
        modifiedBy = username,
        timeOfCreation = LocalDateTime.now(),
        timeOfModification = LocalDateTime.now(),

        emailAddress = emailAddress,
        personId = personId,
        username = username,
        userType = UserDao.UserType.ACTIVE
    )
}
