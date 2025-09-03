package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogBuilder
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.GuestBook
import com.github.jactor.persistence.GuestBookBuilder
import com.github.jactor.persistence.GuestBookEntry
import com.github.jactor.persistence.User
import com.github.jactor.persistence.UserBuilder
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNotSameInstanceAs
import assertk.assertions.isNull

internal class PersistentEntityTest {
    private lateinit var persistentEntityToTest: PersistentEntity<*>

    @Test
    fun `should be able to copy an address without the id`() {
        persistentEntityToTest = initAddress(
            persistent = Persistent(),
            zipCode = "1001",
            addressLine1 = "somewhere",
            addressLine2 = "out",
            addressLine3 = "there",
            city = "svg",
            country = "NO"
        ).toEntityWithId()

        persistentEntityToTest.id = UUID.randomUUID()

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll {
            assertThat(persistentEntityToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentEntityToTest).isEqualTo(copy)
            assertThat(persistentEntityToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a person without the id`() {
        persistentEntityToTest = initPerson(
            address = initAddress(),
            locale = "us_US",
            firstName = "Bill",
            surname = "Smith", description = "here i am"
        ).toEntityWithId()

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll {
            assertThat(persistentEntityToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentEntityToTest).isEqualTo(copy)
            assertThat(persistentEntityToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a user without the id`() {
        persistentEntityToTest = UserBuilder.new(
            User(persistent = Persistent(), emailAddress = "i.am@home", username = "jactor")
        ).build()

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll {
            assertThat(persistentEntityToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentEntityToTest).isEqualTo(copy)
            assertThat(persistentEntityToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a blog without the id`() {
        persistentEntityToTest = BlogBuilder.new(
            blog = Blog(
                title = "general ignorance",
                user = User()
            )
        ).buildBlogEntity()

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll {
            assertThat(persistentEntityToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentEntityToTest).isEqualTo(copy)
            assertThat(persistentEntityToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a blog entry without the id`() {
        val blogEntry = BlogEntry(
            blog = Blog(),
            creatorName = "jactor",
            entry = "the one",
            persistent = Persistent(),
        )

        persistentEntityToTest = BlogBuilder.new().withEntry(blogEntry = blogEntry).buildBlogEntryEntity()
        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll {
            assertThat(persistentEntityToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentEntityToTest).isEqualTo(copy)
            assertThat(persistentEntityToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a guest book without the id`() {
        persistentEntityToTest = GuestBookBuilder.new(
            guestBook = GuestBook(Persistent(), HashSet(), "enter when applied", User())
        ).buildGuestBookEntity()

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll {
            assertThat(persistentEntityToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentEntityToTest).isEqualTo(copy)
            assertThat(persistentEntityToTest).isNotSameInstanceAs(copy)
        }
    }

    @Test
    fun `should be able to copy a guest book entry without the id`() {
        persistentEntityToTest = GuestBookBuilder.new().withEntry(
            GuestBookEntry(
                persistent = Persistent(), guestBook = GuestBook(), creatorName = "jactor", entry = "the one"
            )
        ).buildGuestBookEntryEntity()

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll {
            assertThat(persistentEntityToTest.id).isNotNull()
            assertThat(copy.id).isNull()
            assertThat(persistentEntityToTest).isEqualTo(copy)
            assertThat(persistentEntityToTest).isNotSameInstanceAs(copy)
        }
    }
}
