package com.github.jactor.persistence.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class BlogEntryDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val blogEntryDto = BlogEntryDto()
        blogEntryDto.blog = BlogDto()
        blogEntryDto.creatorName = "someone"
        blogEntryDto.entry = "entry"

        val (_, blog, creatorName, entry) = BlogEntryDto(blogEntryDto.persistentDto, blogEntryDto)

        assertAll(
            { assertThat(blog).`as`("blog").isEqualTo(blogEntryDto.blog) },
            { assertThat(creatorName).`as`("creator name").isEqualTo(blogEntryDto.creatorName) },
            { assertThat(entry).`as`("entry").isEqualTo(blogEntryDto.entry) }
        )
    }

    @Test
    fun `should give values to PersistentDto`() {
        val persistentDto = PersistentDto()
        persistentDto.createdBy = "jactor"
        persistentDto.timeOfCreation = LocalDateTime.now()
        persistentDto.id = 1L
        persistentDto.modifiedBy = "tip"
        persistentDto.timeOfModification = LocalDateTime.now()

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = BlogEntryDto(persistentDto, BlogEntryDto()).persistentDto

        assertAll(
            { assertThat(createdBy).`as`("created by").isEqualTo(persistentDto.createdBy) },
            { assertThat(timeOfCreation).`as`("creation time").isEqualTo(persistentDto.timeOfCreation) },
            { assertThat(id).`as`("id").isEqualTo(persistentDto.id) },
            { assertThat(modifiedBy).`as`("updated by").isEqualTo(persistentDto.modifiedBy) },
            { assertThat(timeOfModification).`as`("updated time").isEqualTo(persistentDto.timeOfModification) }
        )
    }
}
