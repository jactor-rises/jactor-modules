package com.github.jactor.persistence.dto

data class GuestBookModel(
    override val persistentDto: PersistentDto = PersistentDto(),
    var entries: Set<GuestBookEntryModel> = emptySet(),
    var title: String? = null,
    var userInternal: UserModel? = null
) : PersistentDataModel(persistentDto) {
    constructor(
        persistentDto: PersistentDto, guestBook: GuestBookModel
    ) : this(
        persistentDto = persistentDto,
        entries = guestBook.entries,
        title = guestBook.title,
        userInternal = guestBook.userInternal
    )
}
