package com.github.jactor.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.github.jactor.persistence.blog.Blog
import com.github.jactor.persistence.blog.BlogRepository
import com.github.jactor.persistence.test.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.test.initAddress
import com.github.jactor.persistence.test.initPerson
import com.github.jactor.persistence.test.initUser
import com.github.jactor.persistence.user.UserRepository
import java.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired

internal class RepositoriesTest @Autowired constructor(
    private val blogRepository: BlogRepository,
    private val userRepository: UserRepository,
) : AbstractSpringBootNoDirtyContextTest() {

    @BeforeEach
    fun `reset fetch releation`() {
        resetFetchRelations()
    }

    @Test
    fun `should use a BlogRepository to save blogs and find them on on user which was earlier saved`() {
        val address = save(
            address = initAddress(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testoplis"
            )
        )

        val person = save(person = initPerson(address = address, locale = "no_NO", surname = "Skywalker"))
        save(
            user = initUser(
                person = person,
                emailAddress = "brains@rebels.com",
                username = "r2d2"
            )
        )

        var userByUsername = userRepository.findByUsername("r2d2")
            ?: fail { "User not found!" }

        save(
            blog = Blog(
                created = LocalDate.now(),
                title = "Far, far, away...",
                userId = userByUsername.id
            )
        )

        userByUsername = userRepository.findByUsername("r2d2")
            ?: fail { "User not found!" }

        val blogs = blogRepository.findBlogsByUserId(id = userByUsername.id!!)

        assertThat(blogs).hasSize(1)
        val blogDao = blogs.iterator().next()

        assertAll {
            assertThat(blogDao.title).isEqualTo("Far, far, away...")
            assertThat(blogDao.userId).isEqualTo(userByUsername.id)
        }
    }
}