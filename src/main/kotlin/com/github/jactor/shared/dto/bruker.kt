package com.github.jactor.shared.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Metadata for bruker")
data class UserDto(
    @Schema(description = "Identifikator") var id: Long? = null,

    @Schema(description = "Epostadresse") var emailAddress: String? = null,
    @Schema(description = "Persondata") var person: PersonDto? = null,
    @Schema(description = "Brukernavn") var username: String? = null,
    @Schema(description = "Brukerstatus") var userType: UserType = UserType.ACTIVE
)

@Schema(description = "Metadata for ulike brukertyper")
enum class UserType {
    @Schema(description = "Aktiv bruker")
    ACTIVE,

    @Schema(description = "Inaktiv bruker")
    INACTIVE
}

@Schema(description = "Metadata for en person")
data class PersonDto(
    @Schema(description = "Identifikator") var id: Long? = null,

    @Schema(description = "Adressen til en person") var address: AddressDto? = null,
    @Schema(description = "Beskrivelse") var description: String? = null,
    @Schema(description = "Fornavn") var firstName: String? = null,
    @Schema(description = "Internasjonal id (land & språk - ISO/IEC 15897)") var locale: String? = null,
    @Schema(description = "Etternavn") var surname: String? = null
)

@Schema(description = "Metadata for en adresse")
data class AddressDto(
    @Schema(description = "Identifikator") var id: Long? = null,

    @Schema(description = "Addresselinje 1") var addressLine1: String? = null,
    @Schema(description = "Addresselinje 2") var addressLine2: String? = null,
    @Schema(description = "Addresselinje 3") var addressLine3: String? = null,
    @Schema(description = "Poststed") var city: String? = null,
    @Schema(description = "Land") var country: String? = null,
    @Schema(description = "Postnnummer") var zipCode: String? = null
)
