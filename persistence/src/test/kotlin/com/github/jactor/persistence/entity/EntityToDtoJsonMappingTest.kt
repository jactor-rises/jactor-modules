package com.github.jactor.persistence.entity

import org.junit.jupiter.api.Test
import com.github.jactor.persistence.JactorPersistenceConfig
import com.github.jactor.persistence.test.initAddressEntity
import com.github.jactor.persistence.test.initPersonEntity
import com.github.jactor.persistence.test.initUserEntity
import assertk.assertThat
import assertk.assertions.contains

internal class EntityToDtoJsonMappingTest {

    private val objectMapper = JactorPersistenceConfig().objectMapper()

    @Test
    fun `skal mappe json fra UserDto fra UserModel skapt av UserEntity`() {
        val userModel = initUserEntity(person = initPersonEntity(address = initAddressEntity())).asModel()
        val json: String = objectMapper.writeValueAsString(userModel.toDto())

        assertThat(json).contains(""""person":{""",""""address":{""")
    }
}
