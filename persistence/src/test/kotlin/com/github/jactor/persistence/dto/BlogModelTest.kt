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

        val (_, created, title, userInternal) = BlogModel(blogModel.persistentModel, blogModel)

        assertAll {
            assertThat(created).isEqualTo(blogModel.created)
            assertThat(title).isEqualTo(blogModel.title)
            assertThat(userInternal).isEqualTo(blogModel.userInternal)
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

        val (id, createdBy, timeOfCreation, modifiedBy, timeOfModification) = BlogModel(
            persistentModel,
            BlogModel()
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
