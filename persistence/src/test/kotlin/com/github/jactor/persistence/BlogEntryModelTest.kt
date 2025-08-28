package com.github.jactor.persistence

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.common.PersistentModel
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class BlogEntryModelTest {

    @Test
    fun `should have a copy constructor`() {
        val blogEntryModel = BlogEntryModel(
            blog = BlogModel(),
            creatorName = "someone",
            entry = "entry",
        )

        val (blog, creatorName, entry) = BlogEntryModel(blogEntryModel.persistentModel, blogEntryModel)

        assertAll {
            assertThat(blog).isEqualTo(blogEntryModel.blog)
            assertThat(creatorName).isEqualTo(blogEntryModel.creatorName)
            assertThat(entry).isEqualTo(blogEntryModel.entry)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentModel = PersistentModel(
            createdBy = "jactor",
            id = UUID.randomUUID(),
            modifiedBy = "tip",
            timeOfCreation = LocalDateTime.now(),
            timeOfModification = LocalDateTime.now()
        )

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = BlogEntryModel(
            persistentModel = persistentModel,
            blogEntry = BlogEntryModel()
        ).persistentModel

        assertAll {
            assertThat(createdBy).isEqualTo(persistentModel.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistentModel.timeOfCreation)
            assertThat(id).isEqualTo(persistentModel.id)
            assertThat(modifiedBy).isEqualTo(persistentModel.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistentModel.timeOfModification)
        }
    }
}
