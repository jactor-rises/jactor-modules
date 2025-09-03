package com.github.jactor.persistence

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initBlog
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class BlogTest {

    @Test
    fun `should have a copy constructor`() {
        val blog = Blog(
            created = LocalDate.now(),
            title = "title",
            user = User()
        )

        val (created, _, title, userInternal) = Blog(blog.persistent, blog)

        assertAll {
            assertThat(created).isEqualTo(blog.created)
            assertThat(title).isEqualTo(blog.title)
            assertThat(userInternal).isEqualTo(blog.user)
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

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = Blog(
            persistent,
            initBlog()
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
