package com.gitlab.jactor.persistence.dto

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
        if (persistentDto != null) {
            return persistentDto as PersistentDto
        }

        return PersistentDto()
    }

    override fun getId(): Long? {
        return persistentDto?.id
    }

    override fun setId(id: Long) {
        if (persistentDto == null) {
            persistentDto = PersistentDto()
        }

        persistentDto!!.id = id
    }
}
