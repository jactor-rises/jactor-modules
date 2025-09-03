package com.github.jactor.shared.api

import java.time.LocalDateTime
import java.util.UUID
import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
@Schema(description = "Metadata til en blog")
data class BlogDto(
    @Schema(description = "Standard lagrede data") override val persistentDto: PersistentDto = PersistentDto(),
    @Schema(description = "Tittel") val title: String? = null,
    @Schema(description = "Bruker") val user: UserDto? = null,
) : PersistentData

@JvmRecord
@Schema(description = "Metadata til et blogginnlegg")
data class BlogEntryDto(
    @Schema(description = "Standard lagrede data") override val persistentDto: PersistentDto = PersistentDto(),
    @Schema(description = "Blog") val blogDto: BlogDto? = null,
    @Schema(description = "Hvem laget innlegget") val creatorName: String? = null,
    @Schema(description = "Innlegg") val entry: String? = null,
) : PersistentData

@JvmRecord
@Schema(description = "Metadata til en gjestebok")
data class GuestBookDto(
    @Schema(description = "Standard lagrede data") override val persistentDto: PersistentDto = PersistentDto(),
    @Schema(description = "Innlegg i gjestebok") val entries: Set<GuestBookEntryDto> = emptySet(),
    @Schema(description = "Navn for gjestebok") val title: String? = null,
    @Schema(description = "Bruker") val userDto: UserDto? = null
) : PersistentData

@JvmRecord
@Schema(description = "Metadata til et gjestebok-innlegg")
data class GuestBookEntryDto(
    @Schema(description = "Standard lagrede data") override val persistentDto: PersistentDto = PersistentDto(),
    @Schema(description = "Hvem laget innlegget") val creatorName: String? = null,
    @Schema(description = "Innlegg") val entry: String? = null,
    @Schema(description = "Gjesteboka som har innlegget") val guestBook: GuestBookDto? = null,
) : PersistentData

@JvmRecord
@Schema(description = "Metadata for bruker")
data class UserDto(
    @Schema(description = "Standard lagrede data") override val persistentDto: PersistentDto = PersistentDto(),
    @Schema(description = "Epostadresse") val emailAddress: String? = null,
    @Schema(description = "Persondata") val person: PersonDto? = null,
    @Schema(description = "Brukernavn") val username: String? = null,
    @Schema(description = "Brukerstatus") val userType: UserType = UserType.ACTIVE
) : PersistentData

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
    @Schema(description = "Standard lagrede data") override val persistentDto: PersistentDto = PersistentDto(),
    @Schema(description = "Adressen til en person") val address: AddressDto? = null,
    @Schema(description = "Beskrivelse") val description: String? = null,
    @Schema(description = "Fornavn") val firstName: String? = null,
    @Schema(description = "Internasjonal id (land & språk - ISO/IEC 15897)") val locale: String? = null,
    @Schema(description = "Etternavn") val surname: String = ""
) : PersistentData

@JvmRecord
@Schema(description = "Metadata for en adresse")
data class AddressDto(
    @Schema(description = "Standard lagrede data") override val persistentDto: PersistentDto = PersistentDto(),
    @Schema(description = "Addresselinje 1") val addressLine1: String? = null,
    @Schema(description = "Addresselinje 2") val addressLine2: String? = null,
    @Schema(description = "Addresselinje 3") val addressLine3: String? = null,
    @Schema(description = "Poststed") val city: String? = null,
    @Schema(description = "Land") val country: String? = null,
    @Schema(description = "Postnnummer") val zipCode: String? = null,
) : PersistentData

@JvmRecord
@Schema(description = "Alle persistente data som er felles i jactor-modules")
data class PersistentDto(
    @Schema(description = "identifikator til lagrede data") val id: UUID? = null,
    @Schema(description = "brukeren som opprettet dataene") val createdBy: String? = null,
    @Schema(description = "når dataene ble opprettet") val timeOfCreation: LocalDateTime? = null,
    @Schema(description = "brukeren som endret dataene") val modifiedBy: String? = null,
    @Schema(description = "når dataene ble endret") val timeOfModification: LocalDateTime? = null,
)

interface PersistentData {
    val persistentDto: PersistentDto

    fun harIdentifikator(): Boolean = persistentDto.id != null
    fun harIkkeIdentifikator(): Boolean = persistentDto.id == null
}
