package com.github.jactor.persistence.api.controller

import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.UserEntity
import com.github.jactor.shared.api.AddressDto
import com.github.jactor.shared.api.CreateUserCommandDto
import com.github.jactor.shared.api.PersonDto
import com.github.jactor.shared.api.UserDto
import com.github.jactor.shared.api.UserType
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.every

internal class UserControllerTest : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should build full path`() {
        assertThat(buildFullPath("/somewhere")).isEqualTo("http://localhost:$port/jactor-persistence/somewhere")
    }

    @Test
    fun `should not find a user by username`() {
        every { userRepositorySpyk.findByUsername("me") } returns Optional.empty()

        val userRespnse = testRestTemplate.getForEntity(
            buildFullPath("/user/name/me"),
            UserInternalDto::class.java
        )

        assertAll {
            assertThat(userRespnse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
            assertThat(userRespnse.body).isNull()
        }
    }

    @Test
    fun `should find a user by username`() {
        every { userRepositorySpyk.findByUsername("me") } returns Optional.of(UserEntity(UserInternalDto()))

        val userResponse = testRestTemplate.getForEntity(
            buildFullPath("/user/name/me"),
            UserInternalDto::class.java
        )

        assertAll {
            assertThat(userResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(userResponse.body).isNotNull()
        }
    }

    @Test
    fun `should not get a user by id`() {
        val uuid = UUID.randomUUID()
        every { userRepositorySpyk.findById(uuid) } returns Optional.empty()

        val userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/$uuid"), UserInternalDto::class.java)

        assertAll {
            assertThat(userRespnse.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
            assertThat(userRespnse.body).isNull()
        }
    }

    @Test
    fun `should find a user by id`() {
        val uuid = UUID.randomUUID()
        every { userRepositorySpyk.findById(uuid) } returns Optional.of(UserEntity(UserInternalDto()))

        val userRespnse = testRestTemplate.getForEntity(buildFullPath("/user/$uuid"), UserInternalDto::class.java)

        assertAll {
            assertThat(userRespnse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(userRespnse.body).isNotNull()
        }
    }

    @Test
    fun `should modify existing user`() {
        val uuid = UUID.randomUUID()
        val userInternalDto = UserInternalDto()
        userInternalDto.id = uuid

        every { userRepositorySpyk.findById(uuid) } returns Optional.of(UserEntity(userInternalDto))

        val userRespnse = testRestTemplate.exchange(
            buildFullPath("/user/$uuid"), HttpMethod.PUT, HttpEntity(userInternalDto.toUserDto()),
            UserDto::class.java
        )

        assertAll {
            assertThat(userRespnse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
            assertThat(userRespnse.body).isNotNull()
            assertThat(userRespnse.body?.id).isEqualTo(uuid)
        }
    }

    @Test
    fun `should find all usernames on active users`() {
        val bartDto = UserDto(person = PersonDto(address = AddressDto()), username = "bart", userType = UserType.ACTIVE)
        val lisaDto = UserDto(person = PersonDto(address = AddressDto()), username = "lisa", userType = UserType.ACTIVE)
        val bart = UserEntity(UserInternalDto(bartDto))
        val lisa = UserEntity(UserInternalDto(lisaDto))

        every { userRepositorySpyk.findByUserTypeIn(listOf(UserEntity.UserType.ACTIVE)) } returns listOf(bart, lisa)

        val userResponse =
            testRestTemplate.exchange(buildFullPath("/user/usernames"), HttpMethod.GET, null, responsIslistOfStrings())

        assertAll {
            assertThat(userResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(userResponse.body).isEqualTo(listOf("bart", "lisa"))
        }
    }

    @Test
    fun `should accept if user id is valid`() {
        val uuid = UUID.randomUUID()
        every { userRepositorySpyk.findById(uuid) } returns Optional.of(UserEntity(UserInternalDto()))

        val userResponse = testRestTemplate.exchange(
            buildFullPath("/user/$uuid"), HttpMethod.PUT, HttpEntity(UserDto(id = uuid)),
            UserDto::class.java
        )

        assertThat(userResponse.statusCode).isEqualTo(HttpStatus.ACCEPTED)
    }

    @Test
    fun `should not accept if user id is invalid`() {
        every { userRepositorySpyk.findById(any()) } returns Optional.empty()

        val userResponse = testRestTemplate.exchange(
            buildFullPath("/user/${UUID.randomUUID()}"), HttpMethod.PUT, HttpEntity(UserDto()),
            UserDto::class.java
        )

        assertThat(userResponse.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    private fun buildFullPath(url: String): String {
        return "http://localhost:$port$contextPath$url"
    }

    private fun responsIslistOfStrings(): ParameterizedTypeReference<List<String>> {
        return object : ParameterizedTypeReference<List<String>>() {}
    }

    @Test
    fun `should return BAD_REQUEST when username is occupied`() {
        every { userRepositorySpyk.findByUsername("turbo") } returns Optional.of(UserEntity())

        val createUserCommand = CreateUserCommandDto(username = "turbo")

        val userResponse = testRestTemplate.exchange(
            buildFullPath("/user"), HttpMethod.POST, HttpEntity(createUserCommand),
            UserDto::class.java
        )

        assertThat(userResponse.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}