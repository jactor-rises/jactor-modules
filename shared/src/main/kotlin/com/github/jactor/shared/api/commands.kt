package com.github.jactor.shared.api

import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
@Schema(description = "Metadata for creation of a user")
data class CreateUserCommandDto(
    @Schema(description = "The username of a user") val username: String = "",
    @Schema(description = "The surname of a user") val surname: String = "",
    @Schema(description = "The email address of a user") val emailAddress: String? = null,
    @Schema(description = "The description of a user") val description: String? = null,
    @Schema(description = "The first name of a user") val firstName: String? = null,
    @Schema(description = "The language of a user") val language: String? = null,
    @Schema(description = "The users first address line") val addressLine1: String? = null,
    @Schema(description = "The users second address line") val addressLine2: String? = null,
    @Schema(description = "The users third address line") val addressLine3: String? = null,
    @Schema(description = "The zip code of a user") val zipCode: String? = null,
    @Schema(description = "The city of a user") val city: String? = null,
    @Schema(description = "The country of a user") val contry: String? = null
)
