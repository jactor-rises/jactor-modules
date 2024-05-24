package com.github.jactor.persistence.dto

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore

data class GuestBookEntryModel(
    val persistentModel: PersistentModel = PersistentModel(),
    var guestBook: GuestBookModel? = null,
    var creatorName: String? = null,
    var entry: String? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id
    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("No creator is provided!")
    val notNullableEntry: String
        @JsonIgnore get() = entry ?: throw IllegalStateException("No entry is provided!")

    constructor(
        persistentModel: PersistentModel, guestBookEntry: GuestBookEntryModel
    ) : this(
        persistentModel = persistentModel,
        guestBook = guestBookEntry.guestBook,
        creatorName = guestBookEntry.creatorName,
        entry = guestBookEntry.entry
    )
}
