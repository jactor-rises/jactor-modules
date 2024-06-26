package com.github.jactor.web.dto

import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto

class UserModel(private val address: AddressDto?, private val person: PersonDto?, private val user: UserDto) {
    constructor(user: UserDto) : this(address = user.person?.address, person = user.person, user = user)

    fun fetchAddress(): List<String> {
        return listOf(
            address?.addressLine1 ?: "",
            address?.addressLine2 ?: "",
            address?.addressLine3 ?: "",
            address?.zipCode ?: "",
            address?.city ?: ""
        )
    }

    fun fetchFullName(): String {
        val firstName = person?.firstName

        return if (firstName != null) "$firstName ${person?.surname}" else person?.surname ?: ""
    }

    fun fetchUsername() = user.username ?: ""
    fun fetchDescriptionCode() = person?.description
}
