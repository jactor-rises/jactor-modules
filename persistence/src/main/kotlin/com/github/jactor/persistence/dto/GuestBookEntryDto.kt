package com.github.jactor.persistence.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class GuestBookEntryDto(
    override val persistentDto: PersistentDto = PersistentDto(),
    var guestBook: GuestBookDto? = null,
    var creatorName: String? = null,
    var entry: String? = null
) : PersistentData(persistentDto) {
    val notNullableCreator: String
        @JsonIgnore get() = creatorName ?: throw IllegalStateException("No creator is provided!")
    val notNullableEntry: String
        @JsonIgnore get() = entry ?: throw IllegalStateException("No entry is provided!")

    constructor(
        persistentDto: PersistentDto, guestBookEntry: GuestBookEntryDto
    ) : this(
        persistentDto = persistentDto,
        guestBook = guestBookEntry.guestBook,
        creatorName = guestBookEntry.creatorName,
        entry = guestBookEntry.entry
    )
}
