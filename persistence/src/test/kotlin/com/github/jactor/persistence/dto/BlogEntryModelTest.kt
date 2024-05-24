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

        val (_, blog, creatorName, entry) = BlogEntryModel(blogEntryModel.persistentDto, blogEntryModel)

        assertAll {
            assertThat(blog).isEqualTo(blogEntryModel.blog)
            assertThat(creatorName).isEqualTo(blogEntryModel.creatorName)
            assertThat(entry).isEqualTo(blogEntryModel.entry)
        }
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentDto = PersistentDto()
        persistentDto.createdBy = "jactor"
        persistentDto.timeOfCreation = LocalDateTime.now()
        persistentDto.id = UUID.randomUUID()
        persistentDto.modifiedBy = "tip"
        persistentDto.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = BlogEntryModel(
            persistentDto,
            BlogEntryModel()
        ).persistentDto

        assertAll {
            assertThat(createdBy).isEqualTo(persistentDto.createdBy)
            assertThat(timeOfCreation).isEqualTo(persistentDto.timeOfCreation)
            assertThat(id).isEqualTo(persistentDto.id)
            assertThat(modifiedBy).isEqualTo(persistentDto.modifiedBy)
            assertThat(timeOfModification).isEqualTo(persistentDto.timeOfModification)
        }
    }
}
