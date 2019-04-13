package com.github.jactor.persistence.dto

data class GuestBookDto(
        var persistentDto: PersistentDto? = null,
        var entries: Set<GuestBookEntryDto> = emptySet(),
        var title: String? = null,
        var user: UserDto? = null
) : PersistentData(persistentDto) {
    constructor(
            persistent: PersistentDto, guestBook: GuestBookDto
    ) : this(
            persistent, guestBook.entries, guestBook.title, guestBook.user
    )
}
