package com.github.jactor.persistence.dto

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonIgnore

data class GuestBookModel(
    val persistentModel: PersistentModel = PersistentModel(),
    var entries: Set<GuestBookEntryModel> = emptySet(),
    var title: String? = null,
    var userInternal: UserModel? = null
) {
    val id: UUID? @JsonIgnore get() = persistentModel.id

    constructor(
        persistentModel: PersistentModel, guestBook: GuestBookModel
    ) : this(
        persistentModel = persistentModel,
        entries = guestBook.entries,
        title = guestBook.title,
        userInternal = guestBook.userInternal
    )
}
