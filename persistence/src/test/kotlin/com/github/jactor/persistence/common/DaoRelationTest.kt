package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every
import io.mockk.mockk

class DaoRelationTest {
    private var fetchCounter = 0
    private val testRelation = mockk<PersistentDao<*>> {}

    private val daoRelation = DaoRelation(
        fetchRelation = { testRelation.also { fetchCounter++ } },
    )

    @Test
    fun `should not fail when when no id is provided`() {
        val testRelation = daoRelation.fetchRelatedInstance(id = null)

        assertThat(testRelation).isNull()
    }

    @Test
    fun `should fetch related instance`() {
        val uuid = UUID.randomUUID()

        every { testRelation.id } returns uuid

        val fetchedRelationA = daoRelation.fetchRelatedInstance(id = uuid)
        val fetchedRelationB = daoRelation.fetchRelatedInstance(id = uuid)

        assertAll {
            assertThat(fetchedRelationA?.id).isEqualTo(uuid)
            assertThat(fetchedRelationB?.id).isEqualTo(uuid)
            assertThat(fetchCounter).isEqualTo(1)
        }
    }

    @Test
    fun `should not fetch new relation when it is already fetched`() {
        val uuid = UUID.randomUUID()

        every { testRelation.id } returns uuid

        daoRelation.fetchRelatedInstance(id = uuid)
        daoRelation.fetchRelatedInstance(id = uuid)
        daoRelation.fetchRelatedInstance(id = uuid)

        assertThat(fetchCounter).isEqualTo(1)
    }

    @Test
    fun `should fetch a new relation when the id of old related object is not the same`() {
        every { testRelation.id } answers { UUID.randomUUID() }

        val fetchedRelationA = daoRelation.fetchRelatedInstance(id = UUID.randomUUID())
        val fetchedRelationB = daoRelation.fetchRelatedInstance(id = UUID.randomUUID())

        assertAll {
            assertThat(fetchedRelationA?.id).isNotNull().isNotEqualTo(fetchedRelationB?.id)
            assertThat(fetchCounter).isEqualTo(2)
        }
    }
}
