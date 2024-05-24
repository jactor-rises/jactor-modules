package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class BlogEntryModelTest {

    @Test
    fun `should have a copy constructor`() {
        val blogEntryModel = BlogEntryModel()
        blogEntryModel.blog = BlogModel()
        blogEntryModel.creatorName = "someone"
        blogEntryModel.entry = "entry"

        val (_, blog, creatorName, entry) = BlogEntryModel(blogEntryModel.persistentModel, blogEntryModel)

        assertAll {
            assertThat(blog).isEqualTo(blogEntryModel.blog)
            assertThat(creatorName).isEqualTo(blogEntryModel.creatorName)
            assertThat(entry).isEqualTo(blogEntryModel.entry)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentModel = PersistentModel()
        persistentModel.createdBy = "jactor"
        persistentModel.timeOfCreation = LocalDateTime.now()
        persistentModel.id = UUID.randomUUID()
        persistentModel.modifiedBy = "tip"
        persistentModel.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = BlogEntryModel(
            persistentModel,
            BlogEntryModel()
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
