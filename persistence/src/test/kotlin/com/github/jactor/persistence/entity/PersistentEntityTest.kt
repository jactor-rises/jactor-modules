package com.github.jactor.persistence.entity

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
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
            addressInternalDto = AddressInternalDto(
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
            PersonInternalDto(
                address = AddressInternalDto(),
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
            UserInternalDto(persistentDto = PersistentDto(), emailAddress = "i.am@home", username = "jactor")
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
            blogDto = BlogDto(
                title = "general ignorance",
                userInternal = UserInternalDto()
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
        val blogEntryDto = BlogEntryDto(PersistentDto(), BlogDto(), "jactor", "the one")
        persistentEntityToTest = BlogBuilder.new().withEntry(blogEntryDto = blogEntryDto).buildBlogEntryEntity()
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
            guestBookDto = GuestBookDto(PersistentDto(), HashSet(), "enter when applied", UserInternalDto())
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
            GuestBookEntryDto(
                persistentDto = PersistentDto(), guestBook = GuestBookDto(), creatorName = "jactor", entry = "the one"
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
