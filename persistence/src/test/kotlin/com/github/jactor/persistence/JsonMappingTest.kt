package com.github.jactor.persistence

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jactor.persistence.address.AddressRepository
import com.github.jactor.persistence.person.PersonRepository
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initPersonDao
import com.github.jactor.persistence.test.initUserDao
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.PersistentDto
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

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
        val personId = UUID.randomUUID()

        every { personRepositoryMockk.findById(any()) } returns initPersonDao(id = personId)

        val user = initUserDao(personId = personId).toUser()
        val json: String = objectMapper.writeValueAsString(user.toUserDto())

        assertThat(json, "person").contains(""""personId":"$personId"""")
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
            BlogEntryDto(blogId = uuid)
        )

        val blogEntry = objectMapper.readValue(json, BlogEntryDto::class.java)

        assertThat(blogEntry.blogId).isEqualTo(uuid)
    }
}
