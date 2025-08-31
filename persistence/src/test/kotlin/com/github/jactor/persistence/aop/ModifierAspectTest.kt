package com.github.jactor.persistence.aop

import java.time.LocalDateTime
import java.util.UUID
import org.aspectj.lang.JoinPoint
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.BlogBuilder
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.GuestBookBuilder
import com.github.jactor.persistence.GuestBookEntry
import com.github.jactor.persistence.GuestBook
import com.github.jactor.persistence.PersonBuilder
import com.github.jactor.persistence.Person
import com.github.jactor.persistence.UserBuilder
import com.github.jactor.persistence.User
import com.github.jactor.persistence.test.toEntityWithId
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
    private val persistent = Persistent(
        createdBy = "na",
        id = null,
        modifiedBy = "na",
        timeOfCreation = oneMinuteAgo,
        timeOfModification = oneMinuteAgo,
    )

    @MockK
    private lateinit var joinPointMock: JoinPoint

    @Test
    fun `should modify timestamp on address when used`() {
        val addressWithoutId = Address(persistent, Address()).toEntityWithId()
        val address = Address(persistent, Address()).toEntityWithId()

        every { joinPointMock.args } returns arrayOf<Any>(address, addressWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(address.timeOfModification).isStrictlyBetween(
            LocalDateTime.now().minusSeconds(1),
            LocalDateTime.now()
        )
    }

    @Test
    fun `should modify timestamp on blog when used`() {
        val blogWithouId = BlogBuilder.unchanged(Blog(persistent, Blog())).buildBlogEntity()
        val blog = BlogBuilder.new(Blog(persistent, Blog())).buildBlogEntity()

        every { joinPointMock.args } returns arrayOf<Any>(blog, blogWithouId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(blog.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        assertThat(blogWithouId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on blogEntry when used`() {
        val blogEntryWithoutId = BlogBuilder.new().withUnchangedEntry(
            blogEntry = BlogEntry(
                persistent = persistent,
                blog = Blog(
                    persistent = Persistent(id = UUID.randomUUID()),
                    blog = Blog(persistent = Persistent(id = UUID.randomUUID())),
                ),
                creatorName = "me",
                entry = "some shit"
            )
        ).buildBlogEntryEntity()

        val blogEntry = BlogBuilder.new().withEntry(
            BlogEntry(persistent, BlogEntry(creatorName = "me", entry = "some shit"))
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
            guestBook = GuestBook(persistent, GuestBook())
        ).buildGuestBookEntity()

        val guestBook = GuestBookBuilder.new(guestBook = GuestBook(persistent, GuestBook()))
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
            guestBookEntry = GuestBookEntry(
                persistent, GuestBookEntry(creatorName = "me", entry = "hi there")
            )
        ).buildGuestBookEntryEntity()

        val guestBookEntry = GuestBookBuilder.new().withEntry(
            guestBookEntry = GuestBookEntry(
                persistent, GuestBookEntry(creatorName = "me", entry = "hi there")
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
        val person = PersonBuilder.new(Person(persistent, Person())).build()
        val personWithoutId = PersonBuilder.unchanged(Person(persistent, Person()))
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
        val user = UserBuilder.new(User(persistent, User())).build()
        val userWithoutId = UserBuilder.unchanged(User(persistent, User()))
            .build()

        every { joinPointMock.args } returns arrayOf<Any>(user, userWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(user.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        assertThat(userWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }
}
