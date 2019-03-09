package com.github.jactor.persistence.dto

data class GuestBookDto(
        var persistentDto: PersistentDto? = null,
        var entries: Set<GuestBookEntryDto> = emptySet(),
        var title: String? = null,
        var user: UserDto? = null
) : Persistent {
    constructor(
            persistent: PersistentDto, guestBook: GuestBookDto
    ) : this(
            persistent, guestBook.entries, guestBook.title, guestBook.user
    )

    override fun fetchPersistentDto(): PersistentDto {
        if (persistentDto == null) {
            persistentDto = PersistentDto()
        }

        return persistentDto as PersistentDto
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long?) {
        persistentDto = setPersistentId(id)
    }
}
