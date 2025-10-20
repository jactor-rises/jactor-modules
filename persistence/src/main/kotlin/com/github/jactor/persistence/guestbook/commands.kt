package com.github.jactor.persistence.guestbook

import java.util.UUID

@JvmRecord
data class CreateGuestBook(
    val userId: UUID,
    val title: String,
)

@JvmRecord
data class CreateGuestBookEntry(
    val guestBookId: UUID,
    val creatorName: String,
    val entry: String,
)
