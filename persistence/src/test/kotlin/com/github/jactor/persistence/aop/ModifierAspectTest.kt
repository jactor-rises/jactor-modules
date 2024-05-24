package com.github.jactor.persistence.aop

import java.time.LocalDateTime
import java.util.UUID
import org.aspectj.lang.JoinPoint
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.GuestBookDto
import com.github.jactor.persistence.dto.GuestBookEntryDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.AddressBuilder
import com.github.jactor.persistence.entity.BlogBuilder
import com.github.jactor.persistence.entity.GuestBookBuilder
import com.github.jactor.persistence.entity.PersonBuilder
import com.github.jactor.persistence.entity.UserBuilder
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isStrictlyBetween
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension

@ExtendWith(MockKExtension::class)
internal class ModifierAspectTest {

    private val oneMinuteAgo = LocalDateTime.now().minusMinutes(1)
    private val modifierAspect = ModifierAspect()
    private val persistentDto = PersistentDto(null, "na", oneMinuteAgo, "na", oneMinuteAgo)

    @MockK
    private lateinit var joinPointMock: JoinPoint

    @Test
    fun `should modify timestamp on address when used`() {
        val addressWithoutId = AddressBuilder.unchanged(
            addressInternalDto = AddressInternalDto(persistentDto, AddressInternalDto())
        ).build()

        val address = AddressBuilder.new(addressInternalDto = AddressInternalDto(persistentDto, AddressInternalDto()))
            .build()

        every { joinPointMock.args } returns arrayOf<Any>(address, addressWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(address.timeOfModification).isStrictlyBetween(
            LocalDateTime.now().minusSeconds(1),
            LocalDateTime.now()
        )

        assertThat(addressWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on blog when used`() {
        val blogWithouId = BlogBuilder.unchanged(BlogDto(persistentDto, BlogDto())).buildBlogEntity()
        val blog = BlogBuilder.new(BlogDto(persistentDto, BlogDto())).buildBlogEntity()

        every { joinPointMock.args } returns arrayOf<Any>(blog, blogWithouId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(blog.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        assertThat(blogWithouId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on blogEntry when used`() {
        val blogEntryWithoutId = BlogBuilder.new().withUnchangedEntry(
            blogEntryDto = BlogEntryDto(
                persistentDto = persistentDto,
                blogEntry = BlogEntryDto(
                    blog = BlogDto(persistentDto = PersistentDto(id = UUID.randomUUID())),
                    creatorName = "me",
                    entry = "some shit"
                )
            )
        ).buildBlogEntryEntity()

        val blogEntry = BlogBuilder.new().withEntry(
            BlogEntryDto(persistentDto, BlogEntryDto(creatorName = "me", entry = "some shit"))
        ).buildBlogEntryEntity()

        every { joinPointMock.args } returns arrayOf<Any>(blogEntry, blogEntryWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(blogEntry.timeOfModification).isStrictlyBetween(
            LocalDateTime.now().minusSeconds(1),
            LocalDateTime.now()
        )

        assertThat(blogEntryWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on guestBook when used`() {
        val guestBookWithoutId = GuestBookBuilder.unchanged(
            guestBookDto = GuestBookDto(persistentDto, GuestBookDto())
        ).buildGuestBookEntity()

        val guestBook = GuestBookBuilder.new(guestBookDto = GuestBookDto(persistentDto, GuestBookDto()))
            .buildGuestBookEntity()

        every { joinPointMock.args } returns arrayOf<Any>(guestBook, guestBookWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(guestBook.timeOfModification).isStrictlyBetween(
            LocalDateTime.now().minusSeconds(1),
            LocalDateTime.now()
        )

        assertThat(guestBookWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on guestBookEntry when used`() {
        val guestBookEntryWithoutId = GuestBookBuilder.new().withEntryContainingPersistentId(
            guestBookEntryDto = GuestBookEntryDto(
                persistentDto, GuestBookEntryDto(creatorName = "me", entry = "hi there")
            )
        ).buildGuestBookEntryEntity()

        val guestBookEntry = GuestBookBuilder.new().withEntry(
            guestBookEntryDto = GuestBookEntryDto(
                persistentDto, GuestBookEntryDto(creatorName = "me", entry = "hi there")
            )
        ).buildGuestBookEntryEntity()

        every { joinPointMock.args } returns arrayOf<Any>(guestBookEntry, guestBookEntryWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(guestBookEntry.timeOfModification).isStrictlyBetween(
            LocalDateTime.now().minusSeconds(1),
            LocalDateTime.now()
        )

        assertThat(guestBookEntryWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on person when used`() {
        val person = PersonBuilder.new(PersonInternalDto(persistentDto, PersonInternalDto())).build()
        val personWithoutId = PersonBuilder.unchanged(PersonInternalDto(persistentDto, PersonInternalDto()))
            .build()

        every { joinPointMock.args } returns arrayOf<Any>(person, personWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(person.timeOfModification).isStrictlyBetween(
            LocalDateTime.now().minusSeconds(1),
            LocalDateTime.now()
        )

        assertThat(personWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on user when used`() {
        val user = UserBuilder.new(UserInternalDto(persistentDto, UserInternalDto())).build()
        val userWithoutId = UserBuilder.unchanged(UserInternalDto(persistentDto, UserInternalDto()))
            .build()

        every { joinPointMock.args } returns arrayOf<Any>(user, userWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(user.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        assertThat(userWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }
}
