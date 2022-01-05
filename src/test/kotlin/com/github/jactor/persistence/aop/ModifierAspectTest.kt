package com.github.jactor.persistence.aop

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
import org.aspectj.lang.JoinPoint
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class ModifierAspectTest {
    private val oneMinuteAgo = LocalDateTime.now().minusMinutes(1)
    private val modifierAspect = ModifierAspect()
    private val persistentDto = PersistentDto(null, "na", oneMinuteAgo, "na", oneMinuteAgo)

    @Mock
    private val joinPointMock: JoinPoint? = null

    @Test
    fun `should modify timestamp on address when used`() {
        val addressWithoutId = anAddress(AddressInternalDto(persistentDto, AddressInternalDto()))
        val address = anAddress(AddressInternalDto(persistentDto, AddressInternalDto()))
        address.id = 1L
        Mockito.`when`(joinPointMock!!.args).thenReturn(arrayOf<Any>(address, addressWithoutId))
        modifierAspect.modifyPersistentEntity(joinPointMock)
        Assertions.assertThat(address.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        Assertions.assertThat(addressWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on blog when used`() {
        val blogWithouId = aBlog(BlogDto(persistentDto, BlogDto()))
        val blog = aBlog(BlogDto(persistentDto, BlogDto()))
        blog.id = 1L
        Mockito.`when`(joinPointMock!!.args).thenReturn(arrayOf<Any>(blog, blogWithouId))
        modifierAspect.modifyPersistentEntity(joinPointMock)
        Assertions.assertThat(blog.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        Assertions.assertThat(blogWithouId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on blogEntry when used`() {
        val blogEntryWithoutId = aBlogEntry(BlogEntryDto(persistentDto, BlogEntryDto()))
        val blogEntry = aBlogEntry(BlogEntryDto(persistentDto, BlogEntryDto()))
        blogEntry.id = 1L
        Mockito.`when`(joinPointMock!!.args).thenReturn(arrayOf<Any>(blogEntry, blogEntryWithoutId))
        modifierAspect.modifyPersistentEntity(joinPointMock)
        Assertions.assertThat(blogEntry.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        Assertions.assertThat(blogEntryWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on guestBook when used`() {
        val guestBookWithoutId = aGuestBook(GuestBookDto(persistentDto, GuestBookDto()))
        val guestBook = aGuestBook(GuestBookDto(persistentDto, GuestBookDto()))
        guestBook.id = 1L
        Mockito.`when`(joinPointMock!!.args).thenReturn(arrayOf<Any>(guestBook, guestBookWithoutId))
        modifierAspect.modifyPersistentEntity(joinPointMock)
        Assertions.assertThat(guestBook.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        Assertions.assertThat(guestBookWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on guestBookEntry when used`() {
        val guestBookEntryWithoutId = aGuestBookEntry(GuestBookEntryDto(persistentDto, GuestBookEntryDto()))
        val guestBookEntry = aGuestBookEntry(GuestBookEntryDto(persistentDto, GuestBookEntryDto()))
        guestBookEntry.id = 1L
        Mockito.`when`(joinPointMock!!.args).thenReturn(arrayOf<Any>(guestBookEntry, guestBookEntryWithoutId))
        modifierAspect.modifyPersistentEntity(joinPointMock)
        Assertions.assertThat(guestBookEntry.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        Assertions.assertThat(guestBookEntryWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on person when used`() {
        val personWithoutId = aPerson(PersonInternalDto(persistentDto, PersonInternalDto()))
        val person = aPerson(PersonInternalDto(persistentDto, PersonInternalDto()))
        person.id = 1L
        Mockito.`when`(joinPointMock!!.args).thenReturn(arrayOf<Any>(person, personWithoutId))
        modifierAspect.modifyPersistentEntity(joinPointMock)
        Assertions.assertThat(person.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        Assertions.assertThat(personWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }

    @Test
    fun `should modify timestamp on user when used`() {
        val userWithoutId = aUser(UserInternalDto(persistentDto, UserInternalDto()))
        val user = aUser(UserInternalDto(persistentDto, UserInternalDto()))
        user.id = 1L
        Mockito.`when`(joinPointMock!!.args).thenReturn(arrayOf<Any>(user, userWithoutId))
        modifierAspect.modifyPersistentEntity(joinPointMock)
        Assertions.assertThat(user.timeOfModification).isStrictlyBetween(LocalDateTime.now().minusSeconds(1), LocalDateTime.now())
        Assertions.assertThat(userWithoutId.timeOfModification).isEqualTo(oneMinuteAgo)
    }
}