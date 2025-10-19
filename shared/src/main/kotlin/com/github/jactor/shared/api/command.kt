package com.github.jactor.shared.api

import java.time.LocalDateTime
import java.util.UUID
import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
@Schema(description = "Metadata for creation of a user")
data class CreateUserCommand(
    @param:Schema(description = "Possible id to the persons address") val addressId: UUID? = null,
    @param:Schema(description = "Possible id to the person represented by this user") val personId: UUID? = null,
    @param:Schema(description = "The username of a user") val username: String = "",
    @param:Schema(description = "The surname of a user") val surname: String = "",
    @param:Schema(description = "The email address of a user") val emailAddress: String? = null,
    @param:Schema(description = "The description of a user") val description: String? = null,
    @param:Schema(description = "The first name of a user") val firstName: String? = null,
    @param:Schema(description = "The language of a user") val language: String? = null,
    @param:Schema(description = "The users first address line") val addressLine1: String? = null,
    @param:Schema(description = "The users second address line") val addressLine2: String? = null,
    @param:Schema(description = "The users third address line") val addressLine3: String? = null,
    @param:Schema(description = "The zip code of a user") val zipCode: String? = null,
    @param:Schema(description = "The city of a user") val city: String? = null,
    @param:Schema(description = "The country of a user") val country: String? = null
) {
    fun toUserDto() = UserDto(
        persistentDto = toPersistentDto(),
        personId = personId,
        emailAddress = emailAddress,
        username = username
    )

    fun toPersonDto() = PersonDto(
        persistentDto = toPersistentDto(id = personId),
        addressId = addressId,
        locale = language,
        firstName = firstName,
        surname = surname,
        description = description
    )

    private fun toAddressDto(): AddressDto? = zipCode?.let {
        AddressDto(
            persistentDto = PersistentDto(id = addressId),
            zipCode = zipCode,
            addressLine1 = addressLine1,
            addressLine2 = addressLine2,
            addressLine3 = addressLine3,
            city = city,
            country = country
        )
    }

    private fun toPersistentDto(id: UUID? = null) = PersistentDto(
        id = id,
        createdBy = username,
        modifiedBy = username,
        timeOfCreation = LocalDateTime.now(),
        timeOfModification = LocalDateTime.now(),
    )
}

@JvmRecord
@Schema(description = "Metadata for creation of a blog entry")
data class CreateBlogEntryCommand(
    @param:Schema(description = "The blog where the entry is placed") val blogId: UUID? = null,
    @param:Schema(description = "The creator of the entry") val creatorName: String? = null,
    @param:Schema(description = "The blog entry") val entry: String? = null,
)

@JvmRecord
@Schema(description = "Metadata for creation of a guest book")
data class CreateGuestBookCommand(
    @param:Schema(description = "The title of the guest book") val title: String? = null,
    @param:Schema(description = "The user id of the owner of the guest book") val userId: UUID? = null,
)

@JvmRecord
@Schema(description = "Metadata for creation of a guest book entry")
data class CreateGuestBookEntryCommand(
    @param:Schema(description = "The guest book where the entry is placed") val guestBookId: UUID? = null,
    @param:Schema(description = "The creator of the entry") val creatorName: String? = null,
    @param:Schema(description = "The guest book entry") val entry: String? = null,
)

@JvmRecord
@Schema(description = "Metadata for modification of a blog title")
data class UpdateBlogTitleCommand(
    @param:Schema(description = "The id of the blog to update") val blogId: UUID? = null,
    @param:Schema(description = "The title to change") val title: String? = null,
)
