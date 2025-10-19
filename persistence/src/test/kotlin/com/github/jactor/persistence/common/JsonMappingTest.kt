package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jactor.persistence.AddressRepository
import com.github.jactor.persistence.JactorPersistenceRepositiesConfig
import com.github.jactor.persistence.PersonRepository
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddressDao
import com.github.jactor.persistence.test.initPersonDao
import com.github.jactor.persistence.test.initUserDao
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.PersistentDto
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import io.mockk.every

internal class JsonMappingTest @Autowired constructor(
    @MockkBean private val addressRepositoryMockk: AddressRepository,
    private val objectMapper: ObjectMapper,
    @MockkBean private val personRepositoryMockk: PersonRepository,
) : AbstractSpringBootNoDirtyContextTest() {
    init {
        JactorPersistenceRepositiesConfig.fetchPersonRelation = { personRepositoryMockk.findById(id = it) }
        JactorPersistenceRepositiesConfig.fetchAddressRelation = { addressRepositoryMockk.findById(id = it) }
    }

    @Test
    fun `skal mappe json fra UserDto fra User skapt av UserDao`() {
        val addressId = UUID.randomUUID()
        val personId = UUID.randomUUID()

        every { addressRepositoryMockk.findById(any()) } returns initAddressDao(id = addressId)
        every { personRepositoryMockk.findById(any()) } returns initPersonDao(id = personId, addressId = addressId)

        val user = initUserDao(personId = personId).toUser()
        val json: String = objectMapper.writeValueAsString(user.toUserDto())

        assertAll {
            assertThat(json, "person").contains(""""person":{""")
            assertThat(json, "address").contains(""""address":{""")
        }
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
