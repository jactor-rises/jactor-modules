package com.github.jactor.persistence.user

import java.util.UUID

internal object UserBuilder {
    fun new(userDto: UserModel): UserData = UserData(
        userDto = userDto.copy(persistentModel = userDto.persistentModel.copy(id = UUID.randomUUID()))
    )

    fun unchanged(userModel: UserModel): UserData = UserData(
        userDto = userModel
    )

    @JvmRecord
    data class UserData(val userDto: UserModel) {
        fun build(): UserEntity = UserEntity(user = userDto)
    }
}
