package com.github.jactor.persistence.dto

data class GuestBookEntryDto(
        var persistentDto: PersistentDto? = null,
        var guestBook: GuestBookDto? = null,
        var creatorName: String? = null,
        var entry: String? = null
) : Persistent {
    constructor(
            persistent: PersistentDto,
            guestBookEntry: GuestBookEntryDto
    ) : this(
            persistent, guestBookEntry.guestBook, guestBookEntry.creatorName, guestBookEntry.entry
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
