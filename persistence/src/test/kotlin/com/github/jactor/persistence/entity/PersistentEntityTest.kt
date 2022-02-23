package com.github.jactor.persistence.entity

import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.AddressEntity.Companion.anAddress
import com.github.jactor.persistence.entity.BlogEntity.Companion.aBlog
import com.github.jactor.persistence.entity.BlogEntryEntity.Companion.aBlogEntry
import com.github.jactor.persistence.entity.GuestBookEntity.Companion.aGuestBook
import com.github.jactor.persistence.entity.GuestBookEntryEntity.Companion.aGuestBookEntry
import com.github.jactor.persistence.entity.PersonEntity.Companion.aPerson
import com.github.jactor.persistence.entity.UserEntity.Companion.aUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test

internal class PersistentEntityTest {
    private lateinit var persistentEntityToTest: PersistentEntity<*>

    @Test
    fun `should be able to copy an address without the id`() {
        persistentEntityToTest = anAddress(
            AddressInternalDto(
                persistentDto = PersistentDto(),
                zipCode = "1001",
                addressLine1 = "somewhere",
                addressLine2 = "out",
                addressLine3 = "there",
                city = "svg",
                country = "NO"
            )
        )

        persistentEntityToTest.id = 1L

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll(
            { assertThat(persistentEntityToTest.id).`as`("id of persistent entity").isEqualTo(1L) },
            { assertThat(copy.id).`as`("id of copy").isNull() },
            { assertThat(persistentEntityToTest).`as`("persistent entity equals copy").isEqualTo(copy) },
            { assertThat(persistentEntityToTest).`as`("persistent entity is not same instance as copy").isNotSameAs(copy) }
        )
    }

    @Test
    fun `should be able to copy a person without the id`() {
        persistentEntityToTest = aPerson(
            PersonInternalDto(
                persistentDto = PersistentDto(),
                address = AddressInternalDto(),
                locale = "us_US",
                firstName = "Bill",
                surname = "Smith", description = "here i am"
            )
        )

        persistentEntityToTest.id = 1L

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll(
            { assertThat(persistentEntityToTest.id).`as`("id of persistent entity").isEqualTo(1L) },
            { assertThat(copy.id).`as`("id of copy").isNull() },
            { assertThat(persistentEntityToTest).`as`("persistent entity equals copy").isEqualTo(copy) },
            { assertThat(persistentEntityToTest).`as`("persistent entity is not same instance as copy").isNotSameAs(copy) }
        )
    }

    @Test
    fun `should be able to copy a user without the id`() {
        persistentEntityToTest = aUser(UserInternalDto(persistentDto = PersistentDto(), emailAddress = "i.am@home", username = "jactor"))
        persistentEntityToTest.id = 1L

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll(
            { assertThat(persistentEntityToTest.id).`as`("id of persistent entity").isEqualTo(1L) },
            { assertThat(copy.id).`as`("id of copy").isNull() },
            { assertThat(persistentEntityToTest).`as`("persistent entity equals copy").isEqualTo(copy) },
            { assertThat(persistentEntityToTest).`as`("persistent entity is not same instance as copy").isNotSameAs(copy) }
        )
    }

    @Test
    fun `should be able to copy a blog without the id`() {
        persistentEntityToTest = aBlog(BlogDto(persistentDto = PersistentDto(), title = "general ignorance", userInternal = UserInternalDto()))
        persistentEntityToTest.id = 1L

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll(
            { assertThat(persistentEntityToTest.id).`as`("id of persistent entity").isEqualTo(1L) },
            { assertThat(copy.id).`as`("id of copy").isNull() },
            { assertThat(persistentEntityToTest).`as`("persistent entity equals copy").isEqualTo(copy) },
            { assertThat(persistentEntityToTest).`as`("persistent entity is not same instance as copy").isNotSameAs(copy) }
        )
    }

    @Test
    fun `should be able to copy a blog entry without the id`() {
        val blogEntryDto = BlogEntryDto(PersistentDto(), BlogDto(), "jactor", "the one")
        persistentEntityToTest = aBlogEntry(blogEntryDto)
        persistentEntityToTest.id = 1L

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll(
            { assertThat(persistentEntityToTest.id).`as`("id of persistent entity").isEqualTo(1L) },
            { assertThat(copy.id).`as`("id of copy").isNull() },
            { assertThat(persistentEntityToTest).`as`("persistent entity equals copy").isEqualTo(copy) },
            { assertThat(persistentEntityToTest).`as`("persistent entity is not same instance as copy").isNotSameAs(copy) }
        )
    }

    @Test
    fun `should be able to copy a guest book without the id`() {
        persistentEntityToTest = aGuestBook(GuestBookDto(PersistentDto(), HashSet(), "enter when applied", UserInternalDto()))
        persistentEntityToTest.id = 1L

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll(
            { assertThat(persistentEntityToTest.id).`as`("id of persistent entity").isEqualTo(1L) },
            { assertThat(copy.id).`as`("id of copy").isNull() },
            { assertThat(persistentEntityToTest).`as`("persistent entity equals copy").isEqualTo(copy) },
            { assertThat(persistentEntityToTest).`as`("persistent entity is not same instance as copy").isNotSameAs(copy) }
        )
    }

    @Test
    fun `should be able to copy a guest book entry without the id`() {
        persistentEntityToTest = aGuestBookEntry(
            GuestBookEntryDto(persistentDto = PersistentDto(), guestBook = GuestBookDto(), creatorName = "jactor", entry = "the one")
        )

        persistentEntityToTest.id = 1L

        val copy = persistentEntityToTest.copyWithoutId() as PersistentEntity<*>

        assertAll(
            { assertThat(persistentEntityToTest.id).`as`("id of persistent entity").isEqualTo(1L) },
            { assertThat(copy.id).`as`("id of copy").isNull() },
            { assertThat(persistentEntityToTest).`as`("persistent entity equals copy").isEqualTo(copy) },
            { assertThat(persistentEntityToTest).`as`("persistent entity is not same instance as copy").isNotSameAs(copy) }
        )
    }
}
