package com.github.jactor.persistence.common

import java.time.LocalDate
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogRepository
import com.github.jactor.persistence.User
import com.github.jactor.persistence.UserRepository
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class RepositoriesTest @Autowired constructor(
    private val blogRepository: BlogRepository,
    private val userRepository: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @Test
    fun `should use a BlogRepository to save a blogs and find them on on user which was earlier saved`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testoplis"
        ).withId()

        val person = initPerson(address = address, locale = "no_NO", surname = "Skywalker").withId()
        val userToPersist = initUser(
            person = person,
            emailAddress = "brains@rebels.com",
            username = "r2d2"
        ).withId().toEntity()

        flush { userRepository.save(userToPersist) }

        var userByUsername = userRepository.findByUsername("r2d2")
            .orElseThrow { AssertionError("User not found!") }

        flush {
            blogRepository.save(
                Blog(
                    created = LocalDate.now(),
                    title = "Far, far, away...",
                    user = userByUsername.toModel()
                ).withId().toEntity()
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