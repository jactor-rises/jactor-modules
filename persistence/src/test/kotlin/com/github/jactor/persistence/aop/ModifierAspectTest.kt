package com.github.jactor.persistence.aop

import java.time.LocalDateTime
import java.util.UUID
import org.aspectj.lang.JoinPoint
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.github.jactor.persistence.address.AddressModel
import com.github.jactor.persistence.dto.BlogModel
import com.github.jactor.persistence.dto.BlogEntryModel
import com.github.jactor.persistence.dto.GuestBookModel
import com.github.jactor.persistence.dto.GuestBookEntryModel
import com.github.jactor.persistence.dto.PersistentModel
import com.github.jactor.persistence.dto.PersonModel
import com.github.jactor.persistence.dto.UserModel
import com.github.jactor.persistence.address.AddressBuilder
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
    private val persistentModel = PersistentModel(
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
        val addressWithoutId = AddressBuilder.unchanged(
            addressModel = AddressModel(persistentModel, AddressModel())
        ).build()

        val address = AddressBuilder.new(addressModel = AddressModel(persistentModel, AddressModel()))
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
        val blogWithouId = BlogBuilder.unchanged(BlogModel(persistentModel, BlogModel())).buildBlogEntity()
        val blog = BlogBuilder.new(BlogModel(persistentModel, BlogModel())).buildBlogEntity()

        every { joinPointMock.args } returns arrayOf<Any>(blog, blogWithouId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(blog.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        assertThat(blogWithouId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on blogEntry when used`() {
        val blogEntryWithoutId = BlogBuilder.new().withUnchangedEntry(
            blogEntryModel = BlogEntryModel(
                persistentModel = persistentModel,
                blog = BlogModel(
                    persistentModel = PersistentModel(id = UUID.randomUUID()),
                    blog = BlogModel(persistentModel = PersistentModel(id = UUID.randomUUID())),
                ),
                creatorName = "me",
                entry = "some shit"
            )
        ).buildBlogEntryEntity()

        val blogEntry = BlogBuilder.new().withEntry(
            BlogEntryModel(persistentModel, BlogEntryModel(creatorName = "me", entry = "some shit"))
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
            guestBookModel = GuestBookModel(persistentModel, GuestBookModel())
        ).buildGuestBookEntity()

        val guestBook = GuestBookBuilder.new(guestBookModel = GuestBookModel(persistentModel, GuestBookModel()))
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
            guestBookEntryModel = GuestBookEntryModel(
                persistentModel, GuestBookEntryModel(creatorName = "me", entry = "hi there")
            )
        ).buildGuestBookEntryEntity()

        val guestBookEntry = GuestBookBuilder.new().withEntry(
            guestBookEntryModel = GuestBookEntryModel(
                persistentModel, GuestBookEntryModel(creatorName = "me", entry = "hi there")
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
        val person = PersonBuilder.new(PersonModel(persistentModel, PersonModel())).build()
        val personWithoutId = PersonBuilder.unchanged(PersonModel(persistentModel, PersonModel()))
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
        val user = UserBuilder.new(UserModel(persistentModel, UserModel())).build()
        val userWithoutId = UserBuilder.unchanged(UserModel(persistentModel, UserModel()))
            .build()

        every { joinPointMock.args } returns arrayOf<Any>(user, userWithoutId)

        modifierAspect.modifyPersistentEntity(joinPointMock)

        assertThat(user.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        assertThat(userWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }
}
