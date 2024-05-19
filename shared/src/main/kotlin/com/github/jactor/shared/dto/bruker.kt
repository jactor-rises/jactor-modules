package com.github.jactor.shared.dto

import java.util.UUID
import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
@Schema(description = "Metadata for bruker")
data class UserDto(
    @Schema(description = "Identifikator") val id: UUID? = null,
    @Schema(description = "Epostadresse") val emailAddress: String? = null,
    @Schema(description = "Persondata") val person: PersonDto? = null,
    @Schema(description = "Brukernavn") val username: String? = null,
    @Schema(description = "Brukerstatus") val userType: UserType = UserType.ACTIVE
)

@Schema(description = "Metadata for ulike brukertyper")
enum class UserType {
    @Schema(description = "Aktiv bruker")
    ACTIVE,

    @Schema(description = "Inaktiv bruker")
    INACTIVE
}

@JvmRecord
@Schema(description = "Metadata for en person")
data class PersonDto(
    @Schema(description = "Identifikator") val id: UUID? = null,
    @Schema(description = "Adressen til en person") val address: AddressDto? = null,
    @Schema(description = "Beskrivelse") val description: String? = null,
    @Schema(description = "Fornavn") val firstName: String? = null,
    @Schema(description = "Internasjonal id (land & språk - ISO/IEC 15897)") val locale: String? = null,
    @Schema(description = "Etternavn") val surname: String = ""
)

@JvmRecord
@Schema(description = "Metadata for en adresse")
data class AddressDto(
    @Schema(description = "Identifikator") val id: UUID? = null,
    @Schema(description = "Addresselinje 1") val addressLine1: String? = null,
    @Schema(description = "Addresselinje 2") val addressLine2: String? = null,
    @Schema(description = "Addresselinje 3") val addressLine3: String? = null,
    @Schema(description = "Poststed") val city: String? = null,
    @Schema(description = "Land") val country: String? = null,
    @Schema(description = "Postnnummer") val zipCode: String? = null
)
