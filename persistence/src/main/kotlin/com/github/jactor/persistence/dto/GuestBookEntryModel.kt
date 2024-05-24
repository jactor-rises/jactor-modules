package com.github.jactor.persistence.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class GuestBookEntryModel(
    override val persistentDto: PersistentDto = PersistentDto(),
    var guestBook: GuestBookModel? = null,
    var creatorName: String? = null,
    var entry: String? = null
) : PersistentDataModel(persistentDto) {
    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("No creator is provided!")
    val notNullableEntry: String
        @JsonIgnore get() = entry ?: throw IllegalStateException("No entry is provided!")

    constructor(
        persistentDto: PersistentDto, guestBookEntry: GuestBookEntryModel
    ) : this(
        persistentDto = persistentDto,
        guestBook = guestBookEntry.guestBook,
        creatorName = guestBookEntry.creatorName,
        entry = guestBookEntry.entry
    )
}
