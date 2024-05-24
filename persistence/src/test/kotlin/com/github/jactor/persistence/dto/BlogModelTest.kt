package com.github.jactor.persistence.dto

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class BlogModelTest {

    @Test
    fun `should have a copy constructor`() {
        val blogModel = BlogModel()
        blogModel.created = LocalDate.now()
        blogModel.title = "title"
        blogModel.userInternal = UserModel()

        val (_, created, title, userInternal) = BlogModel(blogModel.persistentDto, blogModel)

        assertAll {
            assertThat(created).isEqualTo(blogModel.created)
            assertThat(title).isEqualTo(blogModel.title)
            assertThat(userInternal).isEqualTo(blogModel.userInternal)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = BlogModel(
            persistentDto,
            BlogModel()
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
