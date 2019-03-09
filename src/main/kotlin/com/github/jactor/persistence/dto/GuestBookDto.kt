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
        return if (persistentDto != null) persistentDto as PersistentDto else PersistentDto()
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long?) {
        persistentDto = setPersistentId(id)
    }
}
