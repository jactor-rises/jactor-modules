package com.github.jactor.web.model

import com.github.jactor.shared.dto.AddressDto
import com.github.jactor.shared.dto.PersonDto
import com.github.jactor.shared.dto.UserDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@DisplayName("A UserModel")
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
        val testUserModel = UserModel(UserDto(person = PersonDto(address = AddressDto(addressLine1 = "address line 1", zipCode = "1234"))))

        val address = testUserModel.fetchAddress()
        assertThat(address).containsExactly(
            "address line 1",
            "1234"
        )
    }

    @Test
    @DisplayName("should fetch the username of the user")
    fun shouldFetchTheUsername() {
        val testUserModel = UserModel(UserDto(username = "user"))
        assertThat(testUserModel.fetchUsername()).isEqualTo("user")
    }

    @Test
    @DisplayName("should fetch the person behind the user")
    fun shouldFetchThePersonBehindTheUser() {
        val testUserModel = UserModel(UserDto(person = PersonDto(firstName = "John", surname = "Smith", description = "description")))

        assertAll(
            { assertThat(testUserModel.fetchFullName()).isEqualTo("John Smith") },
            { assertThat(testUserModel.fetchDescriptionCode()).isEqualTo("description") }
        )
    }
}
