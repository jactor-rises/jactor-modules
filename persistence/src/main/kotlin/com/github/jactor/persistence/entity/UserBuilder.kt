package com.github.jactor.persistence.entity

import java.util.UUID
import com.github.jactor.persistence.dto.UserModel

internal object UserBuilder {
    fun new(userDto: UserModel): UserData = UserData(
        userDto = userDto.copy(persistentDto = userDto.persistentDto.copy(id = UUID.randomUUID()))
    )

    fun unchanged(userModel: UserModel): UserData = UserData(
        userDto = userModel
    )

    @JvmRecord
    data class UserData(val userDto: UserModel) {
        fun build(): UserEntity = UserEntity(user = userDto)
    }
}
