package com.github.jactor.persistence.common

import java.time.LocalDate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.BlogRepository
import com.github.jactor.persistence.BlogRepositoryObject
import com.github.jactor.persistence.UserRepository
import com.github.jactor.persistence.UserRepositoryObject
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.test.withId
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class RepositoriesTest : AbstractSpringBootNoDirtyContextTest() {
    private val blogRepository: BlogRepository = BlogRepositoryObject
    private val userRepository: UserRepository = UserRepositoryObject

    @Test
    fun `should use a BlogRepository to save a blogs and find them on on user which was earlier saved`() {
        val address = initAddress(
            zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testoplis"
        )

        val person = initPerson(address = address, locale = "no_NO", surname = "Skywalker").withId()
        val userToPersist = initUser(
            person = person,
            emailAddress = "brains@rebels.com",
            username = "r2d2"
        ).toUserDao()

        userRepository.save(userToPersist)

        var userByUsername = userRepository.findByUsername("r2d2")
            ?: fail { "User not found!" }

        blogRepository.save(
            blogDao = Blog(
                created = LocalDate.now(),
                title = "Far, far, away...",
                user = userByUsername.toUser()
            ).toBlogDao()
        )

        userByUsername = userRepository.findByUsername("r2d2")
            ?: fail { "User not found!" }

        val blogs = userByUsername.blogs

        assertThat(blogs).hasSize(1)
        val blogEntity = blogs.iterator().next()

        assertAll {
            assertThat(blogEntity.title).isEqualTo("Far, far, away...")
            assertThat(blogEntity.user).isEqualTo(userByUsername)
        }
    }
}