package com.github.jactor.persistence.dto

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class BlogEntryDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val blogEntryDto = BlogEntryDto()
        blogEntryDto.blog = BlogDto()
        blogEntryDto.creatorName = "someone"
        blogEntryDto.entry = "entry"

        val (_, blog, creatorName, entry) = BlogEntryDto(blogEntryDto.persistentDto, blogEntryDto)

        assertAll {
            assertThat(blog).isEqualTo(blogEntryDto.blog)
            assertThat(creatorName).isEqualTo(blogEntryDto.creatorName)
            assertThat(entry).isEqualTo(blogEntryDto.entry)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = BlogEntryDto(
            persistentDto,
            BlogEntryDto()
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
