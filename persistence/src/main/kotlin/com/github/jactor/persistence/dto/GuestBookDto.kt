package com.github.jactor.persistence.dto

data class GuestBookDto(
    override val persistentDto: PersistentDto = PersistentDto(),
    var entries: Set<GuestBookEntryDto> = emptySet(),
    var title: String? = null,
    var userInternal: UserInternalDto? = null
) : PersistentData(persistentDto) {
    constructor(
        persistentDto: PersistentDto, guestBook: GuestBookDto
    ) : this(
        persistentDto = persistentDto,
        entries = guestBook.entries,
        title = guestBook.title,
        userInternal = guestBook.userInternal
    )
}
