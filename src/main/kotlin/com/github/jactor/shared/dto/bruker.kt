package com.github.jactor.shared.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "Metadata for bruker")
data class UserDto(
        @ApiModelProperty(value = "Identifikator") var id: Long? = null,

        @ApiModelProperty(value = "Epostadresse") var emailAddress: String? = null,
        @ApiModelProperty(value = "Persondata") var person: PersonDto? = null,
        @ApiModelProperty(value = "Brukernavn") var username: String? = null,
        @ApiModelProperty(value = "Brukerstatus") var userType: UserType = UserType.ACTIVE
)

@ApiModel(value = "Metadata for ulike brukertyper")
enum class UserType {
    @ApiModelProperty(value = "Aktiv bruker") ACTIVE,
    @ApiModelProperty(value = "Inaktiv bruker") INACTIVE
}

@ApiModel(value = "Metadata for en person")
data class PersonDto (
        @ApiModelProperty(value = "Identifikator") var id: Long? = null,

        @ApiModelProperty(value = "Adressen til en person") var address: AddressDto? = null,
        @ApiModelProperty(value = "") var description: String? = null,
        @ApiModelProperty(value = "") var firstName: String? = null,
        @ApiModelProperty(value = "") var locale: String? = null,
        @ApiModelProperty(value = "Etternavn") var surname: String? = null
)

@ApiModel(value = "Metadata for en adresse")
data class AddressDto (
        @ApiModelProperty(value = "Identifikator") var id: Long? = null,

        @ApiModelProperty(value = "Addresselinje 1") var addressLine1: String? = null,
        @ApiModelProperty(value = "Addresselinje 2") var addressLine2: String? = null,
        @ApiModelProperty(value = "Addresselinje 3") var addressLine3: String? = null,
        @ApiModelProperty(value = "Poststed") var city: String? = null,
        @ApiModelProperty(value = "Land") var country: String? = null,
        @ApiModelProperty(value = "Postnnummer") var zipCode: String? = null
)
