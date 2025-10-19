package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import io.mockk.mockk

class DaoRelationsTest {
    private var fetchCounter = 0

    private val daoRelations = DaoRelations(
        fetchRelations = { listOf(mockk<PersistentDao<*>> {}).also { fetchCounter++ } },
    )

    @Test
    fun `should fetch related instances by id`() {
        val relationsA = daoRelations.fetchRelations(id = UUID.randomUUID())
        val relationsB = daoRelations.fetchRelations(id = UUID.randomUUID())

        assertAll {
            assertThat(relationsA, "relationsA").hasSize(1)
            assertThat(relationsB, "relationsB").hasSize(1)
            assertThat(relationsA, "relations").isNotEqualTo(relationsB)
            assertThat(fetchCounter, "fetchCounter").isEqualTo(2)
        }
    }

    @Test
    fun `should fetch relations every time as no id can determine state`() {
        val id = UUID.randomUUID()

        daoRelations.fetchRelations(id = id)
        daoRelations.fetchRelations(id = id)
        daoRelations.fetchRelations(id = id)

        assertThat(fetchCounter).isEqualTo(3)
    }
}
