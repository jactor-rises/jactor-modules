package com.github.jactor.shared.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Metadata for creation of a user")
data class CreateUserCommandDto(
    @Schema(description = "The username of a user") var username: String = "",
    @Schema(description = "The surname of a user") var surname: String = "",
    @Schema(description = "The email address of a user") var emailAddress: String? = null,
    @Schema(description = "The description of a user") var description: String? = null,
    @Schema(description = "The first name of a user") var firstName: String? = null,
    @Schema(description = "The language of a user") var language: String? = null,
    @Schema(description = "The users first address line") var addressLine1: String? = null,
    @Schema(description = "The users second address line") var addressLine2: String? = null,
    @Schema(description = "The users third address line") var addressLine3: String? = null,
    @Schema(description = "The zip code of a user") var zipCode: String? = null,
    @Schema(description = "The city of a user") var city: String? = null,
    @Schema(description = "The country of a user") var contry: String? = null
)
