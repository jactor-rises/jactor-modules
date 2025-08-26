package com.github.jactor.persistence.common

import java.time.LocalDate
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.persistence.AbstractSpringBootNoDirtyContextTest
import com.github.jactor.persistence.address.AddressModel
import com.github.jactor.persistence.BlogModel
import com.github.jactor.persistence.person.PersonModel
import com.github.jactor.persistence.user.UserModel
import com.github.jactor.persistence.address.AddressBuilder
import com.github.jactor.persistence.BlogBuilder
import com.github.jactor.persistence.BlogRepository
import com.github.jactor.persistence.person.PersonBuilder
import com.github.jactor.persistence.user.UserBuilder
import com.github.jactor.persistence.user.UserRepository
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo

internal class RepositoriesTest : AbstractSpringBootNoDirtyContextTest() {
    @Autowired
    private lateinit var blogRepository: BlogRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should use a BlogRepository to save a blogs and find them on on user which was earlier saved`() {
        val address = AddressBuilder.new(
            addressModel = AddressModel(
                zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testoplis"
            )
        ).addressModel

        val personModel = PersonBuilder.new(
            personModel = PersonModel(address = address, locale = "no_NO", surname = "Skywalker")
        ).personModel

        val userToPersist = UserBuilder.new(
            userDto = UserModel(
                person = personModel,
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
                    blogModel = BlogModel(
                        created = LocalDate.now(),
                        title = "Far, far, away...",
                        user = userByUsername.toModel()
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