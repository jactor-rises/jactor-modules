package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.GuestBookDao
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import io.mockk.every
import io.mockk.mockk

class DaoRelationsTest {
    private var fetchCounter = 0

    private val daoRelations = DaoRelations(
        fetchRelations = { listOf(mockk<PersistentDao<*>> {}).also { fetchCounter++ } },
    )

    @Test
    fun `should fetch related instances`() {
        val guestBookDao = mockk<GuestBookDao> {
            every { id } returns UUID.randomUUID()
        }

        val relationsA = daoRelations.fetchRelationsTo(persistentDao = guestBookDao)
        val relationsB = daoRelations.fetchRelationsTo(persistentDao = guestBookDao)

        assertAll {
            assertThat(relationsA, "relationsA").hasSize(1)
            assertThat(relationsB, "relationsB").hasSize(1)
            assertThat(relationsA, "relations").isNotEqualTo(relationsB)
            assertThat(fetchCounter, "fetchCounter").isEqualTo(2)
        }
    }

    @Test
    fun `should fetch relations every time as no id can determine state`() {
        val guestBookDao = mockk<GuestBookDao> {
            every { id } returns UUID.randomUUID()
        }

        daoRelations.fetchRelationsTo(persistentDao = guestBookDao)
        daoRelations.fetchRelationsTo(persistentDao = guestBookDao)
        daoRelations.fetchRelationsTo(persistentDao = guestBookDao)

        assertThat(fetchCounter).isEqualTo(3)
    }
}
