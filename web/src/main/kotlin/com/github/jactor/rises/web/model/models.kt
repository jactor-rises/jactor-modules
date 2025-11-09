package com.github.jactor.rises.web.model

import com.github.jactor.rises.shared.api.UserDto
import com.github.jactor.rises.web.Technology
import java.util.UUID

@JvmRecord
data class HomePageModel(val technologies: List<Technology> = ArrayList())

@JvmRecord
data class UserModel(
    private val user: UserDto,
    private val personId: UUID = user.personId ?: UUID.randomUUID(),
) {
    val username: String?
        get() = user.username

    constructor(user: UserDto) : this(user = user, personId = user.personId ?: UUID.randomUUID())

    fun fetchUsername(): String? {
        return user.username
    }
}
