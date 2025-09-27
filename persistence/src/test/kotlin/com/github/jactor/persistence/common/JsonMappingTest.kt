package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.AddressRepository
import com.github.jactor.persistence.JactorPersistenceConfig
import com.github.jactor.persistence.PersonRepository
import com.github.jactor.persistence.UserRepositoryObject
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUserDao
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.PersistentDto
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject

internal class JsonMappingTest {

    private val objectMapper = JactorPersistenceConfig().objectMapper()

    @BeforeEach
    fun `mock repositories`() = mockkObject(PersonRepository, AddressRepository)

    @AfterEach
    fun `unmock repositories`() = unmockkObject(PersonRepository, AddressRepository)

    @Test
    fun `skal mappe json fra UserDto fra User skapt av UserDao`() {
        every { PersonRepository.findById(id = any()) } returns initPerson().toPersonDao()
        every { AddressRepository.findById(addressId = any()) } returns initAddress().toAddressDao()

        val user = initUserDao().toUser()
        val json: String = objectMapper.writeValueAsString(user.toUserDto())

        assertThat(json).contains(""""person":{""", """"address":{""")
    }

    @Test
    fun `skal mappe BlogDto til json og tilbake til BlogDto`() {
        val uuid = UUID.randomUUID()
        val json = objectMapper.writeValueAsString(BlogDto(persistentDto = PersistentDto(id = uuid)))
        val blog: BlogDto = objectMapper.readValue(json, BlogDto::class.java)

        assertThat(blog.persistentDto.id).isEqualTo(uuid)
    }

    @Test
    fun `skal mappe BlogEntryDto til jason og tilbake til BlogEntryDto`() {
        val uuid = UUID.randomUUID()
        val json = objectMapper.writeValueAsString(
            BlogEntryDto(blogDto = BlogDto(persistentDto = PersistentDto(id = uuid)))
        )

        val blogEntry = objectMapper.readValue(json, BlogEntryDto::class.java)

        assertThat(blogEntry.blogDto?.persistentDto?.id).isEqualTo(uuid)
    }
}
