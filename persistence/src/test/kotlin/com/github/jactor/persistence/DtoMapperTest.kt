package com.github.jactor.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jactor.shared.dto.UserDto
import com.github.jactor.shared.dto.UserType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class DtoMapperTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should map a user to json`() {
        val userlDto = UserDto()
        userlDto.id = 1L
        userlDto.emailAddress = "some@where"
        userlDto.username = "mine"
        userlDto.userType = UserType.ACTIVE

        assertAll(
            { assertThat(objectMapper.writeValueAsString(userlDto)).`as`("id").contains("\"id\":1") },
            { assertThat(objectMapper.writeValueAsString(userlDto)).`as`("email address").contains("\"emailAddress\":\"some@where\"") },
            { assertThat(objectMapper.writeValueAsString(userlDto)).`as`("username").contains("\"username\":\"mine\"") },
            { assertThat(objectMapper.writeValueAsString(userlDto)).`as`("user type").contains("\"userType\":\"ACTIVE\"") }
        )
    }
}