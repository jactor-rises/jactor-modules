package com.github.jactor.persistence.entity

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.dto.AddressModel
import com.github.jactor.persistence.dto.BlogModel
import com.github.jactor.persistence.dto.BlogEntryModel
import com.github.jactor.persistence.dto.GuestBookModel
import com.github.jactor.persistence.dto.GuestBookEntryModel
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonModel
import com.github.jactor.persistence.dto.UserModel
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
        persistentEntityToTest = AddressBuilder.new(
            addressModel = AddressModel(
                persistentDto = PersistentDto(),
                zipCode = "1001",
                addressLine1 = "somewhere",
                addressLine2 = "out",
                addressLine3 = "there",
                city = "svg",
                country = "NO"
            )
        ).build()

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
        persistentEntityToTest = PersonBuilder.new(
            PersonModel(
                address = AddressModel(),
                locale = "us_US",
                firstName = "Bill",
                surname = "Smith", description = "here i am"
            )
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
    fun `should be able to copy a user without the id`() {
        persistentEntityToTest = UserBuilder.new(
            UserModel(persistentDto = PersistentDto(), emailAddress = "i.am@home", username = "jactor")
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
            blogModel = BlogModel(
                title = "general ignorance",
                userInternal = UserModel()
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
        val blogEntryModel = BlogEntryModel(PersistentDto(), BlogModel(), "jactor", "the one")
        persistentEntityToTest = BlogBuilder.new().withEntry(blogEntryModel = blogEntryModel).buildBlogEntryEntity()
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
            guestBookModel = GuestBookModel(PersistentDto(), HashSet(), "enter when applied", UserModel())
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
            GuestBookEntryModel(
                persistentDto = PersistentDto(), guestBook = GuestBookModel(), creatorName = "jactor", entry = "the one"
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
