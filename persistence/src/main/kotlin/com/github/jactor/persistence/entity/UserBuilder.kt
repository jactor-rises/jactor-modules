package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.UserInternalDto

internal object UserBuilder {
    fun new(userDto: UserInternalDto): UserData = UserData(
        userDto = userDto.copy(persistentDto = userDto.persistentDto.copy(id = UUID.randomUUID()))
    )

    fun unchanged(userInternalDto: UserInternalDto): UserData = UserData(
        userDto = userInternalDto
    )

    @JvmRecord
    data class UserData(val userDto: UserInternalDto) {
        fun build(): UserEntity = UserEntity(user = userDto)
    }
}
