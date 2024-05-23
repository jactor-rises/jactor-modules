package com.github.jactor.persistence.repository

import java.time.LocalDate
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.AddressBuilder
import com.github.jactor.persistence.entity.BlogBuilder
import com.github.jactor.persistence.entity.PersonBuilder
import com.github.jactor.persistence.entity.UserBuilder
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class RepositoriesTest : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should use a BlogRepository to save a blogs and find them on on user which was earlier saved`() {
        val address = AddressBuilder.new(
            addressInternalDto = AddressInternalDto(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testoplis"
            )
        ).addressInternalDto

        val personInternalDto = PersonBuilder.new(
            personInternalDto = PersonInternalDto(address = address, locale = "no_NO", surname = "Skywalker")
        ).personInternalDto

        val userToPersist = UserBuilder.new(
            userDto = UserInternalDto(
                person = personInternalDto,
                emailAddress = "brains@rebels.com",
                username = "r2d2"
            )
        ).build()

        flush { userRepository.save(userToPersist) }

        var userByUsername = userRepository.findByUsername("r2d2")
            .orElseThrow { AssertionError("User not found!") }

        flush {
            blogRepository.save(
                BlogBuilder.new(
                    blogDto = BlogDto(
                        created = LocalDate.now(),
                        title = "Far, far, away...",
                        userInternal = userByUsername.asDto()
                    )
                ).buildBlogEntity()
            )
        }

        userByUsername = userRepository.findByUsername("r2d2")
            .orElseThrow { AssertionError("User not found!") }

        val blogs = userByUsername.getBlogs()

        assertThat(blogs).hasSize(1)
        val blogEntity = blogs.iterator().next()

        assertAll {
            assertThat(blogEntity.title).isEqualTo("Far, far, away...")
            assertThat(blogEntity.user).isEqualTo(userByUsername)
        }
    }
}