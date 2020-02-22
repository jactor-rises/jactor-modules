package com.github.jactor.shared.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "Metadata for creation of a user")
data class CreateUserCommandDto(
        @ApiModelProperty(value = "The username of a user") var username: String = "",
        @ApiModelProperty(value = "The surname of a user") var surname: String = "",
        @ApiModelProperty(value = "The email address of a user") var emailAddress: String? = null,
        @ApiModelProperty(value = "The description of a user") var description: String? = null,
        @ApiModelProperty(value = "The first name of a user") var firstName: String? = null,
        @ApiModelProperty(value = "The language of a user") var language: String? = null,
        @ApiModelProperty(value = "The users first address line") var addressLine1: String? = null,
        @ApiModelProperty(value = "The users second address line") var addressLine2: String? = null,
        @ApiModelProperty(value = "The users third address line") var addressLine3: String? = null,
        @ApiModelProperty(value = "The zip code of a user") var zipCode: String? = null,
        @ApiModelProperty(value = "The city of a user") var city: String? = null,
        @ApiModelProperty(value = "The country of a user") var contry: String? = null
)
