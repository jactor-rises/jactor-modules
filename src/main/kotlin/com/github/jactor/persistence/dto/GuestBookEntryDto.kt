package com.github.jactor.persistence.dto

data class GuestBookEntryDto(
        var persistentDto: PersistentDto? = null,
        var guestBook: GuestBookDto? = null,
        var creatorName: String? = null,
        var entry: String? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, guestBookEntry: GuestBookEntryDto
    ) : this(
            persistent, guestBookEntry.guestBook, guestBookEntry.creatorName, guestBookEntry.entry
    )
}
