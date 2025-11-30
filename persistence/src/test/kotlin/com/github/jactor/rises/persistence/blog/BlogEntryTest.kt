package com.github.jactor.rises.persistence.blog

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.rises.persistence.Persistent
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

internal class BlogEntryTest {
    @Test
    fun `should have a copy constructor`() {
        val blogEntry = BlogEntry(
            blogId = UUID.randomUUID(),
            creatorName = "someone",
            entry = "entry",
        )

        val (blog, creatorName, entry) = BlogEntry(
            persistent = blogEntry.persistent,
            blogId = blogEntry.blogId,
            creatorName = blogEntry.creatorName,
            entry = blogEntry.entry,
        )

        assertAll {
            assertThat(blog).isEqualTo(blogEntry.blogId)
            assertThat(creatorName).isEqualTo(blogEntry.creatorName)
            assertThat(entry).isEqualTo(blogEntry.entry)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistent = Persistent(
            createdBy = "jactor",
            id = UUID.randomUUID(),
            modifiedBy = "tip",
            timeOfCreation = LocalDateTime.now(),
            timeOfModification = LocalDateTime.now(),
        )

        val (id, createdBy, modifiedBy, timeOfCreation, timeOfModification) = BlogEntry(
            persistent = persistent,
            blogId = UUID.randomUUID(),
            creatorName = persistent.createdBy,
            entry = "test",
        ).persistent

        assertAll {
            assertThat(createdBy).isEqualTo(persistent.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistent.timeOfCreation)
            assertThat(id).isEqualTo(persistent.id)
            assertThat(modifiedBy).isEqualTo(persistent.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistent.timeOfModification)
        }
    }
}
