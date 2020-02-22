package com.github.jactor.shared.dto

data class CreateUserCommandDto(
        var username: String = "",
        var surname: String = "",
        var emailAddress: String? = null,
        var description: String? = null,
        var firstName: String? = null,
        var language: String? = null,
        var addressLine1: String? = null,
        var addressLine2: String? = null,
        var addressLine3: String? = null,
        var zipCode: String? = null,
        var city: String? = null,
        var contry: String? = null
)
