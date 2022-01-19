package com.github.jactor.persistence.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

internal class BlogDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val blogDto = BlogDto()
        blogDto.created = LocalDate.now()
        blogDto.title = "title"
        blogDto.userInternal = UserInternalDto()

        val (_, created, title, userInternal) = BlogDto(blogDto.persistentDto, blogDto)

        assertAll(
            { assertThat(created).`as`("created").isEqualTo(blogDto.created) },
            { assertThat(title).`as`("title").isEqualTo(blogDto.title) },
            { assertThat(userInternal).`as`("user").isEqualTo(blogDto.userInternal) }
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = BlogDto(persistentDto, BlogDto()).persistentDto

        assertAll(
            { assertThat(createdBy).`as`("created by").isEqualTo(persistentDto.createdBy) },
            { assertThat(timeOfCreation).`as`("creation time").isEqualTo(persistentDto.timeOfCreation) },
            { assertThat(id).`as`("id").isEqualTo(persistentDto.id) },
            { assertThat(modifiedBy).`as`("updated by").isEqualTo(persistentDto.modifiedBy) },
            { assertThat(timeOfModification).`as`("updated time").isEqualTo(persistentDto.timeOfModification) }
        )
    }
}
