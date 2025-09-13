package com.github.jactor.persistence.aop

import java.time.LocalDateTime
import java.util.UUID
import org.aspectj.lang.JoinPoint
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogEntry
import com.github.jactor.persistence.Person
import com.github.jactor.persistence.User
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initBlog
import com.github.jactor.persistence.test.initBlogEntry
import com.github.jactor.persistence.test.initGuestBook
import com.github.jactor.persistence.test.initGuestBookEntry
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import com.github.jactor.shared.test.isNotOlderThan
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
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
        val addressWithoutId = Address(persistent, initAddress()).withId().toEntity()
        val address = Address(persistent, initAddress()).withId().toEntity()

        every { joinPointMock.args } returns arrayOf<Any>(address, addressWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(address.timeOfModification).isNotOlderThan(seconds = 1)
    }

    @Test
    fun `should modify timestamp on blog when used`() {
        val blogWithouId = Blog(persistent, initBlog()).toDao()
        val blog = Blog(persistent, initBlog()).withId().toDao()

        every { joinPointMock.args } returns arrayOf<Any>(blog, blogWithouId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(blog.timeOfModification).isNotOlderThan(seconds = 1)
        assertThat(blogWithouId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on blogEntry when used`() {
        val blogEntryWithoutId = BlogEntry(
            persistent = persistent,
            blog = Blog(
                persistent = Persistent(id = UUID.randomUUID()),
                blog = initBlog(persistent = Persistent(id = UUID.randomUUID())),
            ),
            creatorName = "me",
            entry = "some shit"
        ).toEntity()

        val blogEntry = BlogEntry(
            persistent, initBlogEntry(
                creatorName = "me",
                entry = "some shit",
                blog = initBlog(persistent = Persistent(id = UUID.randomUUID())),
            )
        ).withId().toEntity()

        every { joinPointMock.args } returns arrayOf<Any>(blogEntry, blogEntryWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertAll {
            assertThat(blogEntry.timeOfModification, name = "with id").isNotOlderThan(seconds = 1)
            assertThat(blogEntryWithoutId.timeOfModification, name = "without id").isEqualTo(oneMinuteAgo)
        }
    }

    @Test
    fun `should modify timestamp on guestBook when used`() {
        val guestBookWithoutId = initGuestBook(persistent = persistent).toEntity()
        val guestBook = initGuestBook(persistent = persistent).withId().toEntity()

        every { joinPointMock.args } returns arrayOf<Any>(guestBook, guestBookWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertAll {
            assertThat(guestBook.timeOfModification, name = "with id").isNotOlderThan(seconds = 1)
            assertThat(guestBookWithoutId.timeOfModification, name = "without id").isEqualTo(oneMinuteAgo)
        }
    }

    @Test
    fun `should modify timestamp on guestBookEntry when used`() {
        val guestBookEntryWithoutId = initGuestBookEntry(
            creatorName = "me",
            entry = "hi there",
            persistent = persistent,
        ).toEntity()

        val guestBookEntry = initGuestBookEntry(
            creatorName = "me",
            entry = "hi there",
            persistent = persistent,
        ).withId().toEntity()

        every { joinPointMock.args } returns arrayOf<Any>(guestBookEntry, guestBookEntryWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertAll {
            assertThat(guestBookEntry.timeOfModification, name = "with id").isNotOlderThan(seconds = 1)
            assertThat(guestBookEntryWithoutId.timeOfModification, name = "without id").isEqualTo(oneMinuteAgo)
        }
    }

    @Test
    fun `should modify timestamp on person when used`() {
        val person = Person(persistent, initPerson(id = UUID.randomUUID())).toEntityWithId()
        val personWithoutId = Person(persistent, initPerson()).toEntity()

        every { joinPointMock.args } returns arrayOf<Any>(person, personWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertAll {
            assertThat(person.timeOfModification, "person with id").isNotOlderThan(seconds = 1)
            assertThat(personWithoutId.timeOfModification, "person without id").isEqualTo(oneMinuteAgo)
        }
    }

    @Test
    fun `should modify timestamp on user when used`() {
        val user = User(persistent, user = initUser()).withId().toEntity()
        val userWithoutId = User(persistent, user = initUser()).toEntity()

        every { joinPointMock.args } returns arrayOf<Any>(user, userWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(user.timeOfModification).isNotOlderThan(seconds = 1)
        assertThat(userWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }
}
