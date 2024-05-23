package com.github.jactor.web.model

import org.junit.jupiter.api.Test
import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo

internal class UserModelTest {
    @Test
    fun `should fetch the address of the user as a list of strings`() {
        val testUserModel = UserModel(
            UserDto(
                person = PersonDto(
                    address = AddressDto(
                        addressLine1 = "address line 1",
                        addressLine2 = "address line 2",
                        addressLine3 = "address line 3",
                        zipCode = "1234",
                        city = "somewhere"
                    )
                )
            )
        )

        val address = testUserModel.fetchAddress()
        assertThat(address).containsExactly(
            "address line 1",
            "address line 2",
            "address line 3",
            "1234",
            "somewhere"
        )
    }

    @Test
    fun `should not fetch parts of address being null`() {
        val testUserModel = UserModel(
            UserDto(
                person = PersonDto(
                    address = AddressDto(
                        addressLine1 = "address line 1",
                        zipCode = "1234"
                    )
                )
            )
        )

        val address = testUserModel.fetchAddress()
        assertThat(address).containsExactly(
            "address line 1",
            "1234"
        )
    }

    @Test
    fun `should fetch the username of the user`() {
        val testUserModel = UserModel(UserDto(username = "user"))
        assertThat(testUserModel.fetchUsername()).isEqualTo("user")
    }

    @Test
    fun `should fetch the person behind the user`() {
        val testUserModel =
            UserModel(UserDto(person = PersonDto(firstName = "John", surname = "Smith", description = "description")))

        assertAll {
            assertThat(testUserModel.fetchFullName()).isEqualTo("John Smith")
            assertThat(testUserModel.fetchDescriptionCode()).isEqualTo("description")
        }
    }
}
