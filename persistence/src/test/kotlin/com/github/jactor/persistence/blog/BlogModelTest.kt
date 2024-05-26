package com.github.jactor.persistence.blog

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import com.github.jactor.persistence.common.PersistentModel
import com.github.jactor.persistence.dto.UserModel
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class BlogModelTest {

    @Test
    fun `should have a copy constructor`() {
        val blogModel = BlogModel(
            created = LocalDate.now(),
            title = "title",
            user = UserModel()
        )

        val (created, _, title, userInternal) = BlogModel(blogModel.persistentModel, blogModel)

        assertAll {
            assertThat(created).isEqualTo(blogModel.created)
            assertThat(title).isEqualTo(blogModel.title)
            assertThat(userInternal).isEqualTo(blogModel.user)
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

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = BlogModel(
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
