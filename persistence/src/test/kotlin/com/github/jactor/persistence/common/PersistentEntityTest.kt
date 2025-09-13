package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initGuestBookEntry
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNotSameInstanceAs
import assertk.assertions.isNull

internal class PersistentEntityTest {
    private lateinit var persistentDaoToTest: PersistentDao<*>

    @Test
    fun `should be able to copy an address without the id`() {
        persistentDaoToTest = initAddress(
            persistent = Persistent(),
            zipCode = "1001",
            addressLine1 = "somewhere",
            addressLine2 = "out",
            addressLine3 = "there",
            city = "svg",
            country = "NO"
        ).withId().toEntity()

        persistentDaoToTest.id = UUID.randomUUID()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest).isEqualTo(copy)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a person without the id`() {
        persistentDaoToTest = initPerson(
            address = initAddress(),
            locale = "us_US",
            firstName = "Bill",
            surname = "Smith", description = "here i am"
        ).toEntityWithId()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest).isEqualTo(copy)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a user without the id`() {
        persistentDaoToTest = initUser(persistent = Persistent(), emailAddress = "i.am@home", username = "jactor")
            .withId().toEntity()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest).isEqualTo(copy)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a blog without the id`() {
        persistentDaoToTest = initBlog(
            title = "general ignorance",
            user = initUser()
        ).withId().toEntity()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest).isEqualTo(copy)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a blog entry without the id`() {
        val blogEntry = BlogEntry(
            blog = initBlog(),
            creatorName = "jactor",
            entry = "the one",
            persistent = Persistent(),
        )

        persistentDaoToTest = blogEntry.withId().toEntity()
        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest).isEqualTo(copy)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a guest book without the id`() {
        persistentDaoToTest = initGuestBook(title = "enter when applied", user = initUser()).withId().toEntity()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest).isEqualTo(copy)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a guest book entry without the id`() {
        persistentDaoToTest = initGuestBookEntry(
            guestBook = initGuestBook(),
            creatorName = "jactor",
            entry = "the one"
        ).withId().toEntity()

        val copy = persistentDaoToTest.copyWithoutId() as PersistentDao<*>

        assertAll {
            assertThat(persistentDaoToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentDaoToTest).isEqualTo(copy)
            assertThat(persistentDaoToTest).isNotSameInstanceAs(copy)
        }
    }
}
