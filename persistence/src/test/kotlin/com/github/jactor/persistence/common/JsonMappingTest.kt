package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jactor.persistence.AddressRepository
import com.github.jactor.persistence.PersonRepository
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUserDao
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.PersistentDto
import com.ninjasquad.springmockk.MockkBean
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import io.mockk.every

internal class JsonMappingTest @Autowired constructor(
    @MockkBean private val addressRepositoryMockk: AddressRepository,
    private val objectMapper: ObjectMapper,
    @MockkBean private val personRepositoryMockk: PersonRepository,
): AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `skal mappe json fra UserDto fra User skapt av UserDao`() {
        every { personRepositoryMockk.findById(id = any()) } returns initPerson().toPersonDao()
        every { addressRepositoryMockk.findById(addressId = any()) } returns initAddress().toAddressDao()

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
