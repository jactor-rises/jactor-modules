package com.github.jactor.rises.persistence.blog

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.rises.persistence.Persistent
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test

internal class BlogTest {

    @Test
    fun `should have a copy constructor`() {
        val blog = Blog(
            created = LocalDate.now(),
            title = "title",
            userId = UUID.randomUUID(),
        )

        val (_, created, title, userId) = blog.copy()

        assertAll {
            assertThat(created).isEqualTo(blog.created)
            assertThat(title).isEqualTo(blog.title)
            assertThat(userId).isEqualTo(blog.userId)
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

        val (id, createdBy, modifiedBy, timeOfCreation, timeOfModification) = Blog(
            persistent = persistent,
            created = LocalDate.now(),
            title = "title",
            userId = null,
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
