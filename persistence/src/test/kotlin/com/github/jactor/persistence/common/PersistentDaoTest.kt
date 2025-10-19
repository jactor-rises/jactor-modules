package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initAddressDao
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initGuestBookEntry
import com.github.jactor.persistence.test.initGuestBookEntryDao
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initPersonDao
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.withId
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNotSameInstanceAs
import assertk.assertions.isNull

internal class PersistentDaoTest {
    private lateinit var persistentDaoToTest: PersistentDao<*>

    @Test
    fun `should be able to copy an address without the id`() {
        persistentDaoToTest = initAddressDao(id = UUID.randomUUID())

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest.timeOfCreation).isEqualTo(copy.timeOfCreation)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a person without the id`() {
        persistentDaoToTest = initPersonDao(id = UUID.randomUUID())

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest.timeOfCreation).isEqualTo(copy.timeOfCreation)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a user without the id`() {
        persistentDaoToTest = initUser(
            persistent = Persistent(id = UUID.randomUUID()),
            emailAddress = "i.am@home",
            username = "jactor"
        ).withId().toUserDao()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest.timeOfCreation).isEqualTo(copy.timeOfCreation)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a blog without the id`() {
        persistentDaoToTest = initBlog(
            title = "general ignorance",
            user = initUser()
        ).withId().toBlogDao()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest.timeOfCreation).isEqualTo(copy.timeOfCreation)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a blog entry without the id`() {
        val blogEntry = BlogEntry(
            blog = initBlog().withId(),
            creatorName = "jactor",
            entry = "the one",
            persistent = Persistent(),
        )

        persistentDaoToTest = blogEntry.withId().toBlogEntryDao()
        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest.timeOfCreation).isEqualTo(copy.timeOfCreation)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a guest book without the id`() {
        persistentDaoToTest = initGuestBook(
            title = "enter when applied",
            user = initUser(persistent = Persistent(id = UUID.randomUUID())),
        ).withId().toGuestBookDao()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest.timeOfCreation).isEqualTo(copy.timeOfCreation)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a guest book entry without the id`() {
        persistentDaoToTest = initGuestBookEntryDao(id = UUID.randomUUID())

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest.timeOfCreation).isEqualTo(copy.timeOfCreation)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }
}
