package com.github.jactor.persistence.repository

import java.time.LocalDate
import org.junit.jupiter.api.Test
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.BlogEntity.Companion.aBlog
import com.github.jactor.persistence.entity.UserEntity.Companion.aUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class RepositoriesTest : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should use a BlogRepository to save a blog with a user that was saved with a UserRepository earlier`() {
        val address = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testoplis")
        val personInternalDto = PersonInternalDto(address = address, locale = "no_NO", surname = "Skywalker")
        val userToPersist = aUser(
            UserInternalDto(
                PersistentDto(),
                personInternalDto,
                emailAddress = "brains@rebels.com",
                username = "r2d2"
            )
        )

        userRepository.save(userToPersist)
        entityManager.flush()
        entityManager.clear()

        val userByUsername = userRepository.findByUsername("r2d2").orElseThrow { AssertionError("User not found!") }

        userByUsername.add(
            aBlog(
                BlogDto(
                    PersistentDto(),
                    LocalDate.now(),
                    "Far, far, away...",
                    userByUsername.asDto()
                )
            )
        )

        blogRepository.saveAll(userByUsername.getBlogs())
        entityManager.flush()
        entityManager.clear()

        val blogsByTitle = blogRepository.findBlogsByTitle("Far, far, away...")

        assertThat(blogsByTitle).hasSize(1)
        val blogEntity = blogsByTitle.iterator().next()

        assertAll {
            assertThat(blogEntity.title).isEqualTo("Far, far, away...")
            assertThat(blogEntity.user).isEqualTo(userByUsername)
        }
    }
}