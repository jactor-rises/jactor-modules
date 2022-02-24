package com.github.jactor.persistence.repository

import com.github.jactor.persistence.dto.AddressInternalDto
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.PersistentDto
import com.github.jactor.persistence.dto.PersonInternalDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.BlogEntity.Companion.aBlog
import com.github.jactor.persistence.entity.UserEntity.Companion.aUser
import javax.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
internal class RepositoriesTest {

    @Autowired
    private lateinit var blogRepository: BlogRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `should use a BlogRepository to save a blog with a user that was saved with a UserRepository earlier`() {
        val address = AddressInternalDto(zipCode = "1001", addressLine1 = "Test Boulevard 1", city = "Testoplis")
        val personInternalDto = PersonInternalDto(address = address, locale = "no_NO", surname = "Skywalker")
        val userToPersist = aUser(UserInternalDto(PersistentDto(), personInternalDto, emailAddress = "brains@rebels.com", username = "r2d2"))

        userRepository.save(userToPersist)
        entityManager.flush()
        entityManager.clear()

        val userByUsername = userRepository.findByUsername("r2d2").orElseThrow { AssertionError("User not found!") }

        userByUsername.add(aBlog(BlogDto(PersistentDto(), LocalDate.now(), "Far, far, away...", userByUsername.asDto())))

        blogRepository.saveAll(userByUsername.getBlogs())
        entityManager.flush()
        entityManager.clear()

        val blogsByTitle = blogRepository.findBlogsByTitle("Far, far, away...")

        assertThat(blogsByTitle).hasSize(1)
        val blogEntity = blogsByTitle.iterator().next()

        assertAll(
            { assertThat(blogEntity.title).`as`("blog.title").isEqualTo("Far, far, away...") },
            { assertThat(blogEntity.user).`as`("blog.user").isEqualTo(userByUsername) }
        )
    }
}