package com.github.jactor.persistence.dto

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class BlogDtoTest {

    @Test
    fun `should have a copy constructor`() {
        val blogDto = BlogDto()
        blogDto.created = LocalDate.now()
        blogDto.title = "title"
        blogDto.userInternal = UserInternalDto()

        val (_, created, title, userInternal) = BlogDto(blogDto.persistentDto, blogDto)

        assertAll {
            assertThat(created).isEqualTo(blogDto.created)
            assertThat(title).isEqualTo(blogDto.title)
            assertThat(userInternal).isEqualTo(blogDto.userInternal)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = BlogDto(
            persistentDto,
            BlogDto()
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
