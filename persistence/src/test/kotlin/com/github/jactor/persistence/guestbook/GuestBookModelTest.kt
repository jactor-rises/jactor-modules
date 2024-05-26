package com.github.jactor.persistence.guestbook

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.dto.PersistentModel
import com.github.jactor.persistence.dto.UserModel
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo

internal class GuestBookModelTest {

    @Test
    fun `should have a copy constructor`() {
        val guestBookModel = GuestBookModel(
            entries = setOf(GuestBookEntryModel()),
            title = "title",
            user = UserModel()
        )

        val (_, entries, title, userInternal) = GuestBookModel(guestBookModel.persistentModel, guestBookModel)

        assertAll {
            assertThat(entries).isEqualTo(guestBookModel.entries)
            assertThat(title).isEqualTo(guestBookModel.title)
            assertThat(userInternal).isEqualTo(guestBookModel.user)
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

        val (createdBy, id, modifiedBy, timeOfCreation, timeOfModification) = GuestBookModel(
            persistentModel, GuestBookModel()
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
