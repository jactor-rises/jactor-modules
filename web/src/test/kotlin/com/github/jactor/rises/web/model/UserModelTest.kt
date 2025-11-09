package com.github.jactor.rises.web.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.rises.shared.api.UserDto
import org.junit.jupiter.api.Test

internal class UserModelTest {
    @Test
    fun `should fetch the username of the user`() {
        val testUserModel = UserModel(UserDto(username = "user"))
        assertThat(testUserModel.username).isEqualTo("user")
    }
}
