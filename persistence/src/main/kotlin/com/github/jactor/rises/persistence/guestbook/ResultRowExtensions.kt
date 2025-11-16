package com.github.jactor.rises.persistence.guestbook

import org.jetbrains.exposed.v1.core.ResultRow
import com.github.jactor.rises.persistence.address.AddressDao
import com.github.jactor.rises.persistence.address.Addresses
import com.github.jactor.rises.persistence.blog.BlogDao
import com.github.jactor.rises.persistence.blog.BlogEntries
import com.github.jactor.rises.persistence.blog.BlogEntryDao
import com.github.jactor.rises.persistence.blog.Blogs

fun ResultRow.toAddressDao(): AddressDao = AddressDao(
    id = this[Addresses.id].value,
    createdBy = this[Addresses.createdBy],
    timeOfCreation = this[Addresses.timeOfCreation],
    modifiedBy = this[Addresses.modifiedBy],
    timeOfModification = this[Addresses.timeOfModification],

    addressLine1 = this[Addresses.addressLine1],
    addressLine2 = this[Addresses.addressLine2],
    addressLine3 = this[Addresses.addressLine3],
    city = this[Addresses.city],
    country = this[Addresses.country],
    zipCode = this[Addresses.zipCode],
)

fun ResultRow.toBlogDao(): BlogDao = BlogDao(
    id = this[Blogs.id].value,
    created = this[Blogs.created],
    createdBy = this[Blogs.createdBy],
    modifiedBy = this[Blogs.modifiedBy],
    timeOfCreation = this[Blogs.timeOfCreation],
    timeOfModification = this[Blogs.timeOfModification],
    title = this[Blogs.title],
    userId = this[Blogs.userId],
)

fun ResultRow.toBlogEntryDao(): BlogEntryDao = BlogEntryDao(
    id = this[BlogEntries.id].value,
    createdBy = this[BlogEntries.createdBy],
    timeOfCreation = this[BlogEntries.timeOfCreation],
    modifiedBy = this[BlogEntries.modifiedBy],
    timeOfModification = this[BlogEntries.timeOfModification],
    creatorName = this[BlogEntries.creatorName],
    entry = this[BlogEntries.entry],
    blogId = this[BlogEntries.blogId]
)

fun ResultRow.toGuestBookDao() = GuestBookDao(
    id = this[GuestBooks.id].value,
    createdBy = this[GuestBooks.createdBy],
    timeOfCreation = this[GuestBooks.timeOfCreation],
    modifiedBy = this[GuestBooks.modifiedBy],
    timeOfModification = this[GuestBooks.timeOfModification],

    title = this[GuestBooks.title],
    userId = this[GuestBooks.userId],
)

fun ResultRow.toGuestBookEntryDao() = GuestBookEntryDao(
    id = this[GuestBookEntries.id].value,
    createdBy = this[GuestBookEntries.createdBy],
    timeOfCreation = this[GuestBookEntries.timeOfCreation],
    modifiedBy = this[GuestBookEntries.modifiedBy],
    timeOfModification = this[GuestBookEntries.timeOfModification],

    guestName = this[GuestBookEntries.guestName],
    entry = this[GuestBookEntries.entry],
    guestBookId = this[GuestBookEntries.guestBookId],
)
