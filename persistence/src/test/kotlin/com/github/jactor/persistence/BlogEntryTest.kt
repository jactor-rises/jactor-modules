package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initBlog
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class BlogEntryTest {

    @Test
    fun `should have a copy constructor`() {
        val blogEntry = BlogEntry(
            blog = initBlog(),
            creatorName = "someone",
            entry = "entry",
        )

        val (blog, creatorName, entry) = BlogEntry(
            persistent = blogEntry.persistent,
            blog = blogEntry.blog,
            creatorName = blogEntry.creatorName,
            entry = blogEntry.entry
        )

        assertAll {
            assertThat(blog).isEqualTo(blogEntry.blog)
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
            timeOfModification = LocalDateTime.now()
        )

        val (id, createdBy, modifiedBy, timeOfCreation, timeOfModification) = BlogEntry(
            persistent = persistent,
            blog = initBlog(),
            creatorName = persistent.createdBy,
            entry = null
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
