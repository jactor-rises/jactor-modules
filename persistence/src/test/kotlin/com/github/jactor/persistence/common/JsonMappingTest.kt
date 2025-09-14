package com.github.jactor.persistence.common

import java.util.UUID
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.JactorPersistenceConfig
import com.github.jactor.persistence.test.initAddressDao
import com.github.jactor.persistence.test.initPersonDao
import com.github.jactor.persistence.test.initUserEntity
import com.github.jactor.shared.api.BlogDto
import com.github.jactor.shared.api.BlogEntryDto
import com.github.jactor.shared.api.PersistentDto
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo

internal class JsonMappingTest {

    private val objectMapper = JactorPersistenceConfig().objectMapper()

    @Test
    fun `skal mappe json fra UserDto fra User skapt av UserEntity`() {
        val user = initUserEntity(person = initPersonDao(address = initAddressDao())).toUser()
        val json: String = objectMapper.writeValueAsString(user.toDto())

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
