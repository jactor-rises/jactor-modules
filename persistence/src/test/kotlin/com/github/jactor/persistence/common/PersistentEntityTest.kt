package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.address.AddressBuilder
import com.github.jactor.persistence.address.AddressModel
import com.github.jactor.persistence.blog.BlogBuilder
import com.github.jactor.persistence.blog.BlogModel
import com.github.jactor.persistence.blog.BlogEntryModel
import com.github.jactor.persistence.guestbook.GuestBookModel
import com.github.jactor.persistence.guestbook.GuestBookEntryModel
import com.github.jactor.persistence.dto.PersonModel
import com.github.jactor.persistence.dto.UserModel
import com.github.jactor.persistence.entity.PersonBuilder
import com.github.jactor.persistence.entity.UserBuilder
import com.github.jactor.persistence.guestbook.GuestBookBuilder
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
                persistentModel = PersistentModel(),
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
            UserModel(persistentModel = PersistentModel(), emailAddress = "i.am@home", username = "jactor")
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
                user = UserModel()
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
        val blogEntryModel = BlogEntryModel(
            blog =  BlogModel(),
            creatorName = "jactor",
            entry = "the one",
            persistentModel = PersistentModel(),
        )

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
            guestBookModel = GuestBookModel(PersistentModel(), HashSet(), "enter when applied", UserModel())
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
                persistentModel = PersistentModel(), guestBook = GuestBookModel(), creatorName = "jactor", entry = "the one"
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
