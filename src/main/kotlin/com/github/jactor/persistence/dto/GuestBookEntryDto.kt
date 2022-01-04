package com.github.jactor.persistence.dto

data class GuestBookEntryDto(
    override val persistentDto: PersistentDto = PersistentDto(),
    var guestBook: GuestBookDto? = null,
    var creatorName: String? = null,
    var entry: String? = null
) : PersistentData(persistentDto) {
    constructor(
        persistentDto: PersistentDto, guestBookEntry: GuestBookEntryDto
    ) : this(
        persistentDto = persistentDto,
        guestBook = guestBookEntry.guestBook,
        creatorName = guestBookEntry.creatorName,
        entry = guestBookEntry.entry
    )
}
